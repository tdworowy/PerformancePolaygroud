import datetime
import random
import string
import re
from locust import HttpUser, TaskSet, task
import csv
import json
from os import  path

def random_string(length: int) -> str:
    """
    :param length: length of new random string.
    :return: random string.
    """
    return ''.join(random.choices(string.ascii_uppercase + string.digits, k=length))


def get_service(self: TaskSet):
    """
    :param self: TaskSet object.
    """
    self.client.get("/")

def put_data(self: TaskSet, data_size: int = 20):
    """
    :param self: TaskSet object.
    :param data_size: length of data string.
    """
    data = {
            "data1": random_string(data_size),
            "data2": random_string(data_size)
           }
    headers = {'Content-Type': 'application/json'}

    keys = re.findall("(?<=key:).*?(?=\")", self.client.get("/data").text)

    response = self.client.put("/%s" % random.choices(keys),  json=data, headers=headers)

    assert response.status_code is 200, "Unexpected response code: " + response.status_code
    assert response.elapsed < datetime.timedelta(seconds=2), "Request took more than 2 second"

def post_data_csv(self: TaskSet):
    """
    :param self: TaskSet object.
    """
    with open(path.join("data","data.csv")) as csv_file:
        csv_reader = csv.reader(csv_file, delimiter=',')
        rows = [row for row in csv_reader]
        random_row_number = random.randrange(1, len(rows)-1)

    data = {
              "data1": rows[random_row_number][0],
              "data2":  rows[random_row_number][1]
            }
    headers = {'Content-Type': 'application/json'}
    response = self.client.post("/data",  json=data, headers=headers)

    assert response.status_code is 200, "Unexpected response code: " + response.status_code
    assert response.elapsed < datetime.timedelta(seconds=2), "Request took more than 2 second"

def post_data(self: TaskSet, data_size: int):
    """
    :param self: TaskSet object.
    :param data_size: length of data string.
    """
    data = {
             "data1": random_string(data_size),
             "data2":  random_string(data_size)
        }
    headers = {'Content-Type': 'application/json'}
    response = self.client.post("/data",  json=data, headers=headers)

    assert response.status_code is 200, "Unexpected response code: " + response.status_code
    assert response.elapsed < datetime.timedelta(seconds=2), "Request took more than 2 second"

def post_data_json(self: TaskSet):
    """
    :param self: TaskSet object.
    """
    with open(path.join("data","data_json.json")) as json_file:
        data = json.load(json_file)
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

class ApiTasks3(TaskSet):
    def on_start(self):
       print("ApiTasks3 start.")

    @task
    def post_data_from_csv(self):
        post_data_csv(self)

    @task
    def post_data_from_json(self):
        post_data_json(self)


class ApiUser1(HttpUser):
    task_set = ApiTasks1
    min_wait = 2000
    max_wait = 5000

class ApiUser2(HttpUser):
    task_set = ApiTasks2
    min_wait = 2000
    max_wait = 5000

class ApiUser3(HttpUser):
    task_set = ApiTasks3
    min_wait = 2000
    max_wait = 5000