import { Utils } from "./utils";
import express from 'express';
const app = express()
const port = 3000

app.get('/', (request:any, response:any) => {
    console.log(`Request GET: ${request}`)
    let text :string = Utils.StringUtils.generateString()
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", 'GET');
    response.send(text)
  })

app.post('/data', (request:any, response:any) => {
    console.log(`Request POST: ${request}`)
    response.setHeader("Access-Control-Allow-Origin", "*");
    response.setHeader("Access-Control-Allow-Methods", 'POST');
    response.send("OK")
  })
  
  app.listen(port, (err:any) => {
    if (err) {
      return console.log('something bad happened', err)
    }
  
    console.log(`service is listening on ${port}`)
  })