import {combineReducers} from "redux";

import widgetConfig from "state/widgetConfig/reducer";

import {
  SET_SERVER_CONFIG_LIST,
  REMOVE_SERVER_CONFIG,
  SET_CONTEXT_LIST
} from "./types";

const reduxStatus = () => "works";

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

const contextList = (state = [], action = {}) => {
  switch (action.type) {
    case SET_CONTEXT_LIST:
      return action.payload.contextList;
    default:
      return state;
  }
};

export default combineReducers({
  reduxStatus,
  serverConfig,
  widgetConfig,
  contextList
});
