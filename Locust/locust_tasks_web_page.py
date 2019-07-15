import datetime
import random
import string
import re
from locust import HttpLocust, TaskSet, task


def random_string(length):
    return ''.join(random.choices(string.ascii_uppercase + string.digits, k=length))

class WebsiteTasks(TaskSet):
    def on_start(self):
       print("Start test.")

    @task
    def get_service(self):
        self.client.get("/")

    @task
    def put_data(self):
        data = {
            "data1": random_string(20),
            "data2": random_string(20)
        }
        headers = {'Content-Type': 'application/json'}

        keys = re.findall("(?<=key:).*?(?=\")", self.client.get("/data").text)

        response = self.client.put("/%s" % random.choices(keys),  json=data, headers=headers)

        assert response.status_code is 200, "Unexpected response code: " + response.status_code
        assert response.elapsed < datetime.timedelta(seconds=2), "Request took more than 2 second"

    @task
    def post_data(self):
        data = {
             "data1": random_string(20),
             "data2":  random_string(20)
        }
        headers = {'Content-Type': 'application/json'}
        response = self.client.post("/data",  json=data, headers=headers)

        assert response.status_code is 200, "Unexpected response code: " + response.status_code
        assert response.elapsed < datetime.timedelta(seconds=2), "Request took more than 2 second"

    @task
    def post_big_data(self):
        data = {
            "data1": random_string(3000),
            "data2": random_string(3000)
        }
        headers = {'Content-Type': 'application/json'}
        response = self.client.post("/data", json=data, headers=headers)

        assert response.status_code is 200, "Unexpected response code: " + response.status_code
        assert response.elapsed < datetime.timedelta(seconds=2), "Request took more than 2 second"

class WebsiteUser(HttpLocust):
    task_set = WebsiteTasks
    min_wait = 2000
    max_wait = 5000