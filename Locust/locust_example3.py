from random import randrange

from locust import HttpUser, task, between

from utils import random_string


class ApiUser1(HttpUser):
    wait_time = between(3, 5)

    @task(3)
    def get_random_string(self):
        self.client.get("/randStr")

    @task(2)
    def post_data(self):
        headers = {'Content-Type': 'application/json'}
        for _ in range(5):
            data_to_post = {"field1":random_string(randrange(30)),
                            "field2":random_string(randrange(30))}

            self.client.post("/postData",  json=data_to_post,  headers=headers)

    @task(1)
    def get_slow(self):
        self.client.get("/slow")


class ApiUser2(HttpUser):
    wait_time = between(5, 10)

    @task(3)
    def get_random_string1(self):
        self.client.get("/randStr")

    @task(2)
    def get_random_string2(self):
        for _ in range(15):
          self.client.get("/randStr")

    @task(1)
    def get_random_string3(self):
        for _ in range(25):
          self.client.get("/randStr")
