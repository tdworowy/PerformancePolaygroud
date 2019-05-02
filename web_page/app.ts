
//import * as WebRequest from 'web-request';  // don't work becous this import

class HttpClient {
    getRequest(aUrl:string, aCallback: any) {
        var anHttpRequest = new XMLHttpRequest();
        anHttpRequest.onreadystatechange = function() { 
            if (anHttpRequest.readyState == 4 && anHttpRequest.status == 200)
                aCallback(anHttpRequest.responseText);
        }

        anHttpRequest.open( "GET", aUrl, true );            
        anHttpRequest.send( null );
    }
}

class RandomString {
    public async render(divId: string) {
        let el: HTMLElement | null = document.getElementById(divId);
        {
            var client = new HttpClient();
            client.getRequest('http://localhost:3000', function(response:any) {
                if(el != null) {
                    el.innerText = response
                }
            }); // STILL DON'T WORK
        }
    }
}
window.onload = () => {
    var randomString = new RandomString();
    randomString.render("content")
};
