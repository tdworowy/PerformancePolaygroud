from locust import HttpLocust, TaskSet, task


class WebsiteTasks(TaskSet):
    def on_start(self):
       print("Start tests.")

    @task
    def blog_main(self):
        self.client.get("http://localhost:8083/blog/")


class WebsiteUser(HttpLocust):
    task_set = WebsiteTasks
    min_wait = 5000
    max_wait = 15000