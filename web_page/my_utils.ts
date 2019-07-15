const uuid = require('uuid/v4');
export class StringUtils {
        static getRandomInt(min: number, max: number) {
            return Math.floor(Math.random() * (max - min)) + min;
        }
        static generateString(from:number=3000,to:number=10000) {
            var text: string="";
            let num: number = StringUtils.getRandomInt(from,to)
            for(var i:number = 0;i<=num;i++) {
                text = text + uuid();
            }
            return text
    }
}
