import { StringUtils } from "./my_utils";
import express from 'express';
var bodyParser = require("body-parser");
const app = express()
const port = 3000
app.use(bodyParser.urlencoded({extended : true}));
app.use(bodyParser.json())

app.get('/', (request:any, response:any) => {
    console.log(`Request GET: ${request}`)
    let text :string = StringUtils.generateString()
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", 'GET');
    response.send(text)
  })
app.post('/data', (request:any, response:any) => {
    console.log(`Request POST`)
    console.log(`data1: ${request.body.data1}`)
    console.log(`data2: ${request.body.data2}`)
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers", "accept, content-type");
    response.send("OK")
  })
app.options('/data', (request:any, response:any) => {
    console.log(`Request POST: ${request.body}`)
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers", "accept, content-type");
    response.send("OK")
  })

app.listen(port, (err:any) => {
    if (err) {
      return console.log('something bad happened', err)
    }
    console.log(`service is listening on ${port}`)
  })