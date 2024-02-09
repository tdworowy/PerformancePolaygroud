from locust import HttpUser,TaskSet,task


class TaskSet1(TaskSet):
    def on_start(self):
       print("Start.")

    @task
    def get_random_string1(self):
        #TODO why it never run ?
        self.client.get("/randStr")

class ApiUser1(HttpUser):
    tasks : {TaskSet1: 1}
    min_wait = 100
    max_wait = 500

    @task
    def dummy_task(self):
        self.client.get("/randStr")

