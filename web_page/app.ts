
import { Utils } from "./utils";

class RandomString {
    public render(divId: string) {
        let el: HTMLElement | null = document.getElementById(divId);
        let text: string = Utils.StringUtils.generateString();
        if(el != null){
            el.innerText = text;
        }
    }
}
window.onload = () => {
    var randomString = new RandomString();
    randomString.render("content")
};
