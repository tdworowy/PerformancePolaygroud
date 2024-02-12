from locust import HttpUser,task

class ApiUser1(HttpUser):
    min_wait = 100
    max_wait = 500

    @task(3)
    def get_random_string(self):
        self.client.get("/randStr")

    @task(2)
    def post_data(self):
        data_to_post = {"field1":"Test123", "field2":"Test456"}
        response = self.client.post("/postData",  json=data_to_post)

        assert response.json() == data_to_post

    @task(1)
    def get_slow(self):
        self.client.get("/slow")



