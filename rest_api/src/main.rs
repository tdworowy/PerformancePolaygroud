use actix_web::{get, post, web, App, HttpResponse, HttpServer, Responder};
use rand::{distributions::Alphanumeric, Rng};
use serde::{Deserialize, Serialize};
use std::{thread, time, usize};

//TODO use tokio_postgress
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

#[derive(Serialize, Deserialize)]
struct Data {
    field1: String,
    field2: String,
}

#[post("/postData")]
async fn post_data(data: web::Json<Data>) -> impl Responder {
    let _data = data.into_inner();
    // let mut client = Client::connect("host=localhost user=test", NoTls).unwrap();
    //  let row = client.query_opt(
    //     "select field1, field2 from test_table where field1 = $1 and field2 = $2",
    //     &[&_data.field1, &_data.field2],
    //  );

    //match row {
    //    Ok(Some(_)) => {}
    //   Ok(None) => {
    //    let _ = client.execute(
    //         "insert into test_table (field1, field2) values ($1, $2)",
    //           &[&_data.field1, &_data.field2],
    //    );
    //  }
    //  Err(error) => println!("Error {:?}", error),
    // };

    HttpResponse::Ok().json(_data)
}

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    HttpServer::new(|| {
        App::new()
            .service(random_string)
            .service(slow)
            .service(post_data)
    })
    .bind(("0.0.0.0", 8080))?
    .run()
    .await
}
