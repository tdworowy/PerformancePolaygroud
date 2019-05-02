var http = require('http');
var fs = require('fs');

const PORT=8081; 

fs.readFile('./index.html', function (err:any, html:any) {

    if (err) {
        console.log(err)
        throw err;
    }    

    http.createServer(function(request:any, response:any) {  
        response.writeHeader(200, {"Content-Type": "text/html"});  
        response.write(html);  
        response.end();  
    }).listen(PORT);
});