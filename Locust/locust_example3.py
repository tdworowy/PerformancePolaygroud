from locust import HttpUser,task

class ApiUser1(HttpUser):
    min_wait = 100
    max_wait = 500

    @task(5)
    def get_random_string1(self):
        self.client.get("/randStr")

