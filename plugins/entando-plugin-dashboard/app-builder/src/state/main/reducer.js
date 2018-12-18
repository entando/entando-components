import {combineReducers} from "redux";

import widgetConfig from "state/widgetConfig/reducer";

import {
  SET_SERVER_CONFIG_LIST,
  REMOVE_SERVER_CONFIG,
  SET_DATASOURCE_LIST,
  SET_DATASOURCE_COLUMNS,
  SET_DATASOURCE_DATA
} from "./types";

const reduxStatus = () => "works";

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
      // const {columns} = action.payload;
      // return columns.reduce((acc, item) => {
      //   acc[item] = item;
      //   return acc;
      // }, {});
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
  dashboardConfig,
  widgetConfig,
  datasourceList,
  datasourceColumns,
  datasourceData
});
