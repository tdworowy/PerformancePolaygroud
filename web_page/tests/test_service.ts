import * as WebRequest from 'web-request';

async function test() {
    var result = await WebRequest.get('http://localhost:3000');
    console.log(result.content)
}
test()