import datetime
import random
import string
import re
from locust import HttpLocust, TaskSet, task


def random_string(length):
    return ''.join(random.choices(string.ascii_uppercase + string.digits, k=length))


def get_service(self):
    """
    :param self: TaskSet object
    """
    self.client.get("/")

def put_data(self):
    """
    :param self: TaskSet object
    """
    data = {
            "data1": random_string(20),
            "data2": random_string(20)
           }
    headers = {'Content-Type': 'application/json'}

    keys = re.findall("(?<=key:).*?(?=\")", self.client.get("/data").text)

    response = self.client.put("/%s" % random.choices(keys),  json=data, headers=headers)

    assert response.status_code is 200, "Unexpected response code: " + response.status_code
    assert response.elapsed < datetime.timedelta(seconds=2), "Request took more than 2 second"

def post_data(self, data_size):
    """
    :param self: TaskSet object
    :param data_size: number

    """
    data = {
             "data1": random_string(data_size),
             "data2":  random_string(data_size)
        }
    headers = {'Content-Type': 'application/json'}
    response = self.client.post("/data",  json=data, headers=headers)

    assert response.status_code is 200, "Unexpected response code: " + response.status_code
    assert response.elapsed < datetime.timedelta(seconds=2), "Request took more than 2 second"

class ApiTasks1(TaskSet):
    def on_start(self):
       print("ApiTasks1 start.")

    @task
    def get_service(self):
        get_service(self)

    @task
    def put_data(self):
        put_data(self)

    @task
    def post_data(self):
        post_data(self,20)


class ApiTasks2(TaskSet):
    def on_start(self):
       print("ApiTasks2 start.")

    @task
    def get_service(self):
        get_service(self)

    @task
    def post_big_data(self):
        post_data(self,3000)


class ApiUser1(HttpLocust):
    task_set = ApiTasks1
    min_wait = 2000
    max_wait = 5000

class ApiUser2(HttpLocust):
    task_set = ApiTasks2
    min_wait = 2000
    max_wait = 5000