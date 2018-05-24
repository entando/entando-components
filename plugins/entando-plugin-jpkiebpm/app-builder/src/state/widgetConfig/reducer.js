import { combineReducers } from 'redux';

import {
  SET_DEPLOYMENT_UNITS_LIST,
  SET_CASE_DEFINITIONS_LIST,
  SET_PROCESS_LIST,
  SET_OVERRIDES,
} from './types';


const deploymentUnits = (state = [], action = {}) => {
  switch (action.type) {
    case SET_DEPLOYMENT_UNITS_LIST:
      return action.payload.list;
    default:
      return state;
  }
};

const caseDefinitions = (state = [], action = {}) => {
  switch (action.type) {
    case SET_CASE_DEFINITIONS_LIST:
      return action.payload.list;
    default:
      return state;
  }
};

const processList = (state = [], action = {}) => {
  switch (action.type) {
    case SET_PROCESS_LIST:
      return action.payload.list;
    default:
      return state;
  }
};

const overrides = (state = [], action = {}) => {
  switch (action.type) {
    case SET_OVERRIDES:
      return action.payload.list;
    default:
      return state;
  }
};


export default combineReducers({
  deploymentUnits,
  caseDefinitions,
  processList,
  overrides,
});
