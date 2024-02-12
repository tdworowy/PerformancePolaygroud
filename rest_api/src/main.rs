use actix_web::{get, post, web, App, HttpResponse, HttpServer, Responder};
use rand::{distributions::Alphanumeric, Rng};
use serde::{Deserialize, Serialize};
use std::{thread, time, usize};

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
