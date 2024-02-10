use actix_web::{get, App, HttpResponse, HttpServer, Responder};
use rand::{distributions::Alphanumeric, Rng};

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

#[actix_web::main]
async fn main() -> std::io::Result<()> {
    HttpServer::new(|| App::new().service(random_string))
        .bind(("0.0.0.0", 8080))?
        .run()
        .await
}
