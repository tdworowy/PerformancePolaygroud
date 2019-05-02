var http = require('http');
var fs = require('fs');

const PORT=8081; 

fs.readFile('./index.html', function (err:any, html:any) {
    console.log(html);
    if (err) {
        console.log(err)
        throw err;
    }    

    http.createServer(function(request:any, response:any) {  
        response.writeHeader(200, {"Content-Type": "text/html"});  
        response.writeHeader(200, {"Accept": "*/*"});  
        response.writeHeader(200, {"Access-Control-Allow-Origin": "http://localhost"});  
        response.writeHeader(200, {"Access-Control-Allow-Methods": "POST, GET, OPTIONS, DELETE"});  
        response.write(html);  
        response.end();
        console.log(response);  
    }).listen(PORT);
    
});
// DON'T WORK
// User server2