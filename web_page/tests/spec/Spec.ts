const myUtils = require('../../my_utils');
describe('Generate string function', () => {
 it('should return random string', () => {
      const uuidLenght = 36
      const result = myUtils.StringUtils.generateString();
      expect(result).not.toBe("");
      expect(result.length).toBeGreaterThan(uuidLenght * 3000)
      expect(result.length).toBeLessThan(uuidLenght * 10000)
    });
  });
