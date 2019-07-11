var connect = require('connect');
var serveStatic = require('serve-static');
var app = connect()
    .use(serveStatic(__dirname))
    .use(function(req:any, res:any){
        console.log(req)
        res.setHeader("Access-Control-Allow-Origin", "*");
        res.setHeader("Access-Control-Allow-Methods", "GET");
        res.setHeader("Access-Control-Allow-Headers", "accept, content-type");
        
    });
app.listen(8081, function(){
    console.log('Server running on 8081...');
});