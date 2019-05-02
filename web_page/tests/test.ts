import "mocha-typescript"
import { expect } from 'chai';
import { Utils } from "../utils"


describe('Generate string function', () => {

    it('should return random string', () => {
      const result = Utils.StringUtils.generateString();
      expect(result).to.not("");
    });
  });
