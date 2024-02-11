from locust import HttpUser,task

class ApiUser1(HttpUser):
    min_wait = 100
    max_wait = 500

    @task(3)
    def get_random_string(self):
        self.client.get("/randStr")

    @task(1)
    def get_slow(self):
        self.client.get("/slow")

