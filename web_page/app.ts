
class HttpClient {
    static getRequest(aUrl:string, aCallback:(response:any) => void ) {
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
    static render(divId: string) {
        var el: HTMLElement | null = document.getElementById(divId);
        
        HttpClient.getRequest('http://localhost:3000', function(response:any) {
                if(el != null) {
                    el.innerText = response
                }
            }); 
    }
}
window.onload = () => {
    RandomString.render("content")
};
