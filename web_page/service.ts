import { StringUtils } from "./my_utils";
import express from 'express';
const uuid = require('uuid/v4');
var bodyParser = require("body-parser");
const app = express()
const port = 3000

let data:any = {}
let keys:any = []

app.use(bodyParser.urlencoded({extended : true}));
app.use(bodyParser.json())

app.get('/', (request:any, response:any) => {
    let text :string = StringUtils.generateString()
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", 'GET');
    response.send(text)
    console.log(`Request GET`)
  })
app.get('/data', (request:any, response:any) => {
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", 'GET');
    console.log(data)
    response.json(data)
    console.log(`Request GET Data`)
  })
  
app.get('/keys', (request:any, response:any) => {
  response.setHeader("Access-Control-Allow-Origin", "*");
  response.setHeader("Access-Control-Allow-Methods", 'GET');
  console.log(keys)
  response.json(keys)
  console.log(`Request GET keys`)
})
app.get('/:key', (request:any, response:any) => {
    
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", 'GET');
    response.send(data[request.params.key])
    console.log(`Request GET ${request.params.key}`)
  })
  app.put('/:key', (request:any, response:any) => {
    
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", 'PUT');
    data[`key:${request.params.key}`] = JSON.stringify(request.body)
    response.send("Ok")
    console.log(`Request PUT: ${JSON.stringify(request.body)}`)
  })
app.post('/data', (request:any, response:any) => {
    console.log(`Request POST: ${JSON.stringify(request.body)}`)
    var key = uuid()
    data[`key:${key}`] = JSON.stringify(request.body)
    keys.push(key)
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", "POST, OPTIONS");
    response.setHeader("Access-Control-Allow-Headers", "accept, content-type");
    response.send("OK")
  })
app.options('/data', (request:any, response:any) => {
    console.log(`Request Options`)
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