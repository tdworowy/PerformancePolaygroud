import http from "k6/http";
import { Httpx } from "https://jslib.k6.io/httpx/0.1.0/index.js";
import { htmlReport } from "https://raw.githubusercontent.com/benc-uk/k6-reporter/main/dist/bundle.js";
import { sleep } from "k6";

export const options = {
   vus: 5,
  duration: "30s",

  summaryTrendStats: ["med", "p(99)", "p(99.9)", "p(99.99)"],
};

const session = new Httpx();
session.setBaseUrl("http://192.168.50.241:8080");

function generateRandomString(length) {
  let result = [];
  const alphabet = "abcdefghijklmnopqrstuvwxyz".split("");
  for (let i = 0; i <= length; i++) {
    result.push(alphabet[Math.floor(Math.random() * alphabet.length)]);
  }
  return result
}

export default function () {
  session.get("/randStr");
  session.get("/slow");

  const params = {
    headers: {
      "Content-Type": "application/json",
    },
  };

  session.post(
    "/postData",
    { field1: generateRandomString(15), field2: generateRandomString(15) },
    params
  );
  sleep(1);
}

export function handleSummary(data) {
  return {
    "summary.html": htmlReport(data),
  };
}
