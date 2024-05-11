vagrant up
mkdir output
vagrant ssh -c "mkdir build"
vagrant scp .cargo default:/home/vagrant/build
vagrant scp src default:/home/vagrant/build
vagrant scp Cargo.lock default:/home/vagrant/build
vagrant scp Cargo.toml default:/home/vagrant/build
vagrant scp dbconfig.toml default:/home/vagrant/build

vagrant ssh -c "rustup update"
vagrant ssh -c "cd build && cargo build --release"

vagrant scp default:/home/vagrant/build/target/release/rest_api output/rest_api

docker build . -t rest_api