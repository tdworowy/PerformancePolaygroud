const uuid = require('uuid/v4');
export class StringUtils {
        static getRandomInt(min: number, max: number) {
            return Math.floor(Math.random() * (max - min)) + min;
        }
        static generateString() {
            var text: string="";
            let num: number = StringUtils.getRandomInt(3000,10000)
            for(var i:number = 0;i<=num;i++) {
                text = text + uuid();
            }
            return text
    }
}
