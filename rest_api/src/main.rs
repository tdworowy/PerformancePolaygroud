use std::fs;
use std::process::exit;

use actix_web::{get, web, App, HttpResponse, HttpServer, Responder};
use rand::{distributions::Alphanumeric, Rng};
use serde::{Deserialize, Serialize};
use std::{thread, time, usize};
use tokio;
use tokio_postgres::{Client, Config, NoTls};

#[get("/randStr")]
async fn random_string() -> impl Responder {
    let str_size: usize = rand::thread_rng().gen_range(100..5000);
    let rand_str: String = rand::thread_rng()
        .sample_iter(&Alphanumeric)
        .take(str_size)
        .map(char::from)
        .collect();

    HttpResponse::Ok().body(rand_str)
}

#[get("/slow")]
async fn slow() -> impl Responder {
    let rand_sleep_time: u64 = rand::thread_rng().gen_range(300..2000);
    thread::sleep(time::Duration::from_millis(rand_sleep_time));
    HttpResponse::Ok()
}

#[derive(Serialize, Deserialize, Debug)]
struct Data {
    field1: String,
    field2: String,
}

async fn post_data(data: web::Json<Data>, client: web::Data<Client>) -> impl Responder {
    let data = data.into_inner();

    let row = client
        .query_one(
            "SELECT field1, field2 FROM test_table WHERE field1 = $1 AND field2 = $2",
            &[&data.field1, &data.field2],
        )
        .await;

    match row {
        Ok(_) => HttpResponse::Ok().json("Data found"),
        Err(_) => {
            if let Err(err) = client
                .execute(
                    "INSERT INTO test_table (field1, field2) VALUES ($1, $2)",
                    &[&data.field1, &data.field2],
                )
                .await
            {
                eprintln!("Error inserting data: {:?}", err);
                return HttpResponse::InternalServerError().finish();
            }
            HttpResponse::Created().json("Data inserted")
        }
    }
}

#[derive(Deserialize)]
struct DbConfig {
    db_config_content: DbcondifContent,
}

#[derive(Deserialize)]
struct DbcondifContent {
    host: String,
    user: String,
    password: String,
    dbname: String,
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    let filename = "dbconfig.toml";

    let contents = match fs::read_to_string(filename) {
        Ok(c) => c,
        Err(_) => {
            eprintln!("Could not read file `{}`", filename);
            exit(1);
        }
    };

    let db_config: DbConfig = match toml::from_str(&contents) {
        Ok(d) => d,
        Err(_) => {
            eprintln!("Unable to load data from `{}`", filename);
            exit(1);
        }
    };

    let (client, connection) = Config::new()
        .host(&db_config.db_config_content.host)
        .user(&db_config.db_config_content.user)
        .password(&db_config.db_config_content.password)
        .dbname(&db_config.db_config_content.dbname)
        .connect(NoTls)
        .await
        .expect("Unable to connect to database");

    tokio::spawn(async move {
        if let Err(e) = connection.await {
            eprintln!("Connection error: {}", e);
        }
    });

    let client = web::Data::new(client);

    HttpServer::new(move || {
        App::new()
            .service(random_string)
            .service(slow)
            .app_data(client.clone())
            .service(web::resource("/postData").route(web::post().to(post_data)))
    })
    .bind(("0.0.0.0", 8080))?
    .run()
    .await
}
