import _ from 'lodash';

const convertToSnakeCase = (obj: any): any => {
  if (_.isArray(obj)) {
    return obj.map(convertToSnakeCase);
  }

  if (_.isObject(obj) && !_.isDate(obj)) {
    const snakeCaseKeys = _.mapKeys(obj, (_v: unknown, key: string) => _.snakeCase(key));
    return _.mapValues(snakeCaseKeys, convertToSnakeCase);
  }

  return obj;
};

export default convertToSnakeCase;
