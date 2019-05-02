var connect = require('connect');
var serveStatic = require('serve-static');
var app = connect()
    .use(serveStatic(__dirname))
    .use(function(req:any, res:any){

        res.setHeader("Access-Control-Allow-Origin", "*");
        res.header('Access-Control-Allow-Methods', 'GET');
        
    });
app.listen(8081, function(){
    console.log('Server running on 8081...');
});