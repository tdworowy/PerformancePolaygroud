import { Guid } from "guid-typescript";
export module Utils
{
    export class StringUtils {
        static getRandomInt(min: number, max: number) {
            return Math.floor(Math.random() * (max - min)) + min;
        }
        static generateString() {
            var text: string="";
            let num: number = StringUtils.getRandomInt(500,1000)
            for(var i:number = 0;i<=num;i++) {
                text = text + Guid.create();
            }
            return text
    }
}
}
