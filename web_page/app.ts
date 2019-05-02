
class HttpClient {
    static getRequest(aUrl:string, aCallback:(response:string) => void ) {
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
        HttpClient.getRequest('http://localhost:3000', function(response:string) {
                if(el != null) {
                    el.innerText = response
                }
            }); 
        }
    }
}
window.onload = () => {
    var randomString = new RandomString();
    randomString.render("content")
};
