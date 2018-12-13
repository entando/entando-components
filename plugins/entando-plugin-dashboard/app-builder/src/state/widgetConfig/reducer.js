import { combineReducers } from 'redux';

import {
  SET_DEPLOYMENT_UNITS_LIST,
} from './types';


const deploymentUnits = (state = [], action = {}) => {
  switch (action.type) {
    case SET_DEPLOYMENT_UNITS_LIST:
      return action.payload.list;
    default:
      return state;
  }
};


export default combineReducers({
  deploymentUnits,
});
