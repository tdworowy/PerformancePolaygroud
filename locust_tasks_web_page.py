import random
import string

from locust import HttpLocust, TaskSet, task


def random_string(length):
    return ''.join(random.choices(string.ascii_uppercase + string.digits, k=length))

class WebsiteTasks(TaskSet):
    def on_start(self):
       print("Start test.")

    @task
    def get_main(self):
        self.client.get("http://localhost:8081/")

    @task
    def get_service(self):
        self.client.get("http://localhost:3000")

    @task
    def post_data(self):
        data = {
             "data1": "TestData1",
             "data2":  "TestData2"
        }
        headers = {'Content-Type': 'application/json'}
        self.client.post("http://localhost:3000/data",  json=data, headers=headers)

    @task
    def post_big_data(self):
        data = {
            "data1": random_string(3000),
            "data2": random_string(3000)
        }
        headers = {'Content-Type': 'application/json'}
        self.client.post("http://localhost:3000/data", json=data, headers=headers)

class WebsiteUser(HttpLocust):
    task_set = WebsiteTasks
    min_wait = 2000
    max_wait = 5000