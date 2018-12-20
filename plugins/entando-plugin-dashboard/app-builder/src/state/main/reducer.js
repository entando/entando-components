import {combineReducers} from "redux";

import {
  SET_INFO_PAGE,
  SET_LANGUAGES,
  SET_SERVER_CONFIG_LIST,
  REMOVE_SERVER_CONFIG,
  SET_DATASOURCE_LIST,
  SET_DATASOURCE_COLUMNS,
  SET_DATASOURCE_DATA
} from "./types";

const reduxStatus = () => "works";

const infoPage = (state = {}, action = {}) => {
  switch (action.type) {
    case SET_INFO_PAGE:
      return action.payload.info;
    default:
      return state;
  }
};

const languages = (state = [], action = {}) => {
  switch (action.type) {
    case SET_LANGUAGES:
      return action.payload.languages;
    default:
      return state;
  }
};

const dashboardConfig = (state = [], action = {}) => {
  switch (action.type) {
    case SET_SERVER_CONFIG_LIST:
      return action.payload.configList;
    case REMOVE_SERVER_CONFIG:
      return state.filter(config => config.id !== action.payload.configId);
    default:
      return state;
  }
};

const datasourceList = (state = [], action = {}) => {
  switch (action.type) {
    case SET_DATASOURCE_LIST:
      return action.payload.datasourceList;
    default:
      return state;
  }
};

const datasourceColumns = (state = [], action = {}) => {
  switch (action.type) {
    case SET_DATASOURCE_COLUMNS:
      return action.payload.columns;
    default:
      return state;
  }
};
const datasourceData = (state = [], action = {}) => {
  switch (action.type) {
    case SET_DATASOURCE_DATA:
      return action.payload.data;
    default:
      return state;
  }
};

export default combineReducers({
  reduxStatus,
  appBuilder: combineReducers({
    infoPage,
    languages
  }),
  dashboardConfig,
  datasourceList,
  datasourceColumns,
  datasourceData
});