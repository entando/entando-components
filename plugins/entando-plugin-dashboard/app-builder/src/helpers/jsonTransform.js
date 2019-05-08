import { isObject } from 'lodash';

const transform = (data, parent = '') =>
  Object.keys(data).reduce((acc, key) => {
    if (isObject(data[key])) {
      return { ...acc, ...transform(data[key], `${key}.`) };
    }
    acc[`${parent}${key}`] = data[key];
    return acc;
  }, {});

export default transform;
