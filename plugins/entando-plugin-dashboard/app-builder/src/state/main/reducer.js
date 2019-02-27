import {combineReducers} from "redux";

import {
  SET_INFO_PAGE,
  SET_LANGUAGES,
  SET_SERVER_CONFIG_LIST,
  ADD_SERVER_CONFIG,
  REMOVE_SERVER_CONFIG,
  SET_DATASOURCE_LIST,
  SET_DATASOURCE_COLUMNS,
  CLEAR_DATASOURCE_COLUMNS,
  SET_DATASOURCE_DATA,
  SET_SELECTED_DATASOURCE,
  CLEAR_SELECTED_DATASOURCE,
  SET_INTERNAL_ROUTE
} from "./types";

const reduxStatus = () => "works";

const internalRoute = (state = "", action = {}) => {
  switch (action.type) {
    case SET_INTERNAL_ROUTE:
      return action.payload.route;
    default:
      return state;
  }
};

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

const servers = (state = [], action = {}) => {
  switch (action.type) {
    case SET_SERVER_CONFIG_LIST:
      return action.payload.serverList;

    case ADD_SERVER_CONFIG:
      return [...state, action.payload.server];

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

const datasourceSelected = (state = "", action = {}) => {
  switch (action.type) {
    case SET_SELECTED_DATASOURCE:
      return action.payload.datasourceId;
    case CLEAR_SELECTED_DATASOURCE:
      return "";

    default:
      return state;
  }
};

const datasourceColumns = (state = [], action = {}) => {
  switch (action.type) {
    case SET_DATASOURCE_COLUMNS:
      return action.payload.columns;
    case CLEAR_DATASOURCE_COLUMNS:
      return [];
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
  internalRoute,
  appBuilder: combineReducers({
    infoPage,
    languages
  }),
  dashboardConfig: combineReducers({
    servers,
    datasourceList,
    datasource: combineReducers({
      selected: datasourceSelected,
      columns: datasourceColumns,
      data: datasourceData
    })
  })
});
