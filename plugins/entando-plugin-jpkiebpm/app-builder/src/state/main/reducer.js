import { combineReducers } from 'redux';

import {
  SET_SERVER_CONFIG_LIST, REMOVE_SERVER_CONFIG, ADD_CONNECTION_OUTCOMES, CLEAN_CONNECTION_OUTCOMES,
} from './types';


const reduxStatus = () => 'works';


const serverConfig = (state = [], action = {}) => {
  switch (action.type) {
    case SET_SERVER_CONFIG_LIST:
      return action.payload.configList;
    case REMOVE_SERVER_CONFIG:
      return state.filter(config => config.id !== action.payload.configId);
    default:
      return state;
  }
};

const connectionOutcomes = (state = {}, action = {}) => {
  switch (action.type) {
    case ADD_CONNECTION_OUTCOMES:
      return { ...state, ...action.payload.outcomes };
    case CLEAN_CONNECTION_OUTCOMES:
      return {};
    default:
      return state;
  }
};

export default combineReducers({
  reduxStatus,
  serverConfig,
  connectionOutcomes,
});
