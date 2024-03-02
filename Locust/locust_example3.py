from random import randrange

from locust import HttpUser,task

from utils import random_string


class ApiUser1(HttpUser):
    min_wait = 500
    max_wait = 2000

    @task(3)
    def get_random_string(self):
        self.client.get("/randStr")

    @task(2)
    def post_data(self):
        data_to_post = {"field1":random_string(randrange(100)),
                        "field2":random_string(randrange(100))}
        headers = {'Content-Type': 'application/json'}

        self.client.post("/postData",  json=data_to_post,  headers=headers)

    @task(1)
    def get_slow(self):
        self.client.get("/slow")



