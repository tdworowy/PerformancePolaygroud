
import * as WebRequest from 'web-request';

class RandomString {
    public async render(divId: string) {
        let el: HTMLElement | null = document.getElementById(divId);
        if(el != null){
            el.innerText = 'WAITING'
            var result = await WebRequest.get('http://localhost:3000'); // Don't work
            el.innerText = result.content
        }
    }
}
window.onload = () => {
    var randomString = new RandomString();
    randomString.render("content")
};
