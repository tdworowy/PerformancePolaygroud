import { Utils } from "./utils";
import http from 'http';
import express from 'express';
const app = express()
const port = 3000

app.get('/', (request:any, response:any) => {
    console.log(`Get: ${request}`)
    let text :string = Utils.StringUtils.generateString()
    response.send(text)
  })
  
  app.listen(port, (err:any) => {
    if (err) {
      return console.log('something bad happened', err)
    }
  
    console.log(`server is listening on ${port}`)
  })