import {getRoute, getParams, getSearchParams, gotoRoute} from "@entando/router";
import {addToast, addErrors, TOAST_ERROR} from "@entando/messages";
import {formattedText} from "@entando/utils";
import {formValueSelector, initialize, change, arrayPush} from "redux-form";
import {get} from "lodash";

import {getLanguages} from "api/appBuilder";

import {
  getServerType,
  getServerConfig,
  deleteServerConfig,
  postServerConfig,
  putServerConfig,
  getDatasources,
  getDatasourceData,
  putDatasourceColumn
} from "api/dashboardConfig";

import {getWidgetConfigSelector} from "state/app-builder/selectors";

import {
  SET_INFO_PAGE,
  SET_LANGUAGES,
  SET_SERVER_CONFIG_LIST,
  SET_SERVER_TYPE,
  ADD_SERVER_CONFIG,
  UPDATE_SERVER_CONFIG,
  REMOVE_SERVER_CONFIG,
  SET_DATASOURCE_LIST,
  SET_DATASOURCE_DATA,
  SET_DATASOURCE_COLUMNS,
  CLEAR_DATASOURCE_COLUMNS,
  SET_SELECTED_DATASOURCE,
  CLEAR_SELECTED_DATASOURCE,
  SET_INTERNAL_ROUTE
} from "./types";

const DATASOURCE_PROPERTY_DATA = "data";
const DATASOURCE_PROPERTY_COLUMNS = "columns";

// Use for TEST
// const CONFIG_CHART = {
//   config:
//     '{"chart":"donut","axis":{"rotated":false,"x":{"type":"indexed"},"y2":{"show":false}},"size":{"width":300,"height":500},"padding":{"top":50,"right":50,"bottom":50,"left":50},"donut":{"min":0,"max":"1000", "width":"10"},"legend":{"position":"bottom"},"title":{"en":"Gauge"},"serverName":"2","datasource":"temperature","columns":{"x":[{"id":1,"key":"temperature1","value":"temperature1","selected":true},{"id":0,"key":"temperature","value":"temperature","selected":true}]},"data":{"type":"gauge","json":[],"keys":{"value":["temperature1","temperature"]}}}'
// };

export const setInfoPage = info => ({
  type: SET_INFO_PAGE,
  payload: {
    info
  }
});

export const setLanguages = languages => ({
  type: SET_LANGUAGES,
  payload: {
    languages
  }
});

export const setServerType = serverTypeList => ({
  type: SET_SERVER_TYPE,
  payload: {
    serverTypeList
  }
});

export const setServerConfigList = serverList => ({
  type: SET_SERVER_CONFIG_LIST,
  payload: {
    serverList
  }
});

export const addServerConfig = server => ({
  type: ADD_SERVER_CONFIG,
  payload: {
    server
  }
});

export const updateServerConfigAction = server => ({
  type: UPDATE_SERVER_CONFIG,
  payload: {
    server
  }
});

export const removeServerConfigSync = configId => ({
  type: REMOVE_SERVER_CONFIG,
  payload: {
    configId
  }
});

export const setDatasourceList = datasourceList => ({
  type: SET_DATASOURCE_LIST,
  payload: {
    datasourceList
  }
});

export const setDatasourceColumns = columns => ({
  type: SET_DATASOURCE_COLUMNS,
  payload: {
    columns
  }
});

export const clearDatasourceColumns = () => ({
  type: CLEAR_DATASOURCE_COLUMNS
});

export const setDatasourceData = data => ({
  type: SET_DATASOURCE_DATA,
  payload: {
    data
  }
});

export const setSelectedDatasource = datasourceId => ({
  type: SET_SELECTED_DATASOURCE,
  payload: {
    datasourceId
  }
});

export const clearSelectedDatasource = () => ({
  type: CLEAR_SELECTED_DATASOURCE
});

export const setInternalRoute = route => ({
  type: SET_INTERNAL_ROUTE,
  payload: {route}
});

// thunk

export const gotoPluginPage = pluginPage => (dispatch, getState) => {
  const state = getState();
  const route = getRoute(state);
  const params = getParams(state);
  const searchParams = getSearchParams(state);
  try {
    gotoRoute(route, params, {...searchParams, pluginPage});
  } catch (error) {
    console.error("error", error);
    dispatch(setInternalRoute(pluginPage));
  }
};

// used for widget table
export const getWidgetConfig = formName => (dispatch, getState) => {
  const state = getState();
  const config = getWidgetConfigSelector(state);
  if (config) {
    const json = JSON.parse(config.config);
    const columns = Object.keys(json.columns).reduce((acc, key) => {
      const obj = {
        key,
        value: get(json.columns, `${key}.label`),
        hidden: get(json.columns, `${key}.hidden`, false)
      };
      acc.push(obj);
      return acc;
    }, []);
    dispatch(fecthDatasourceList(json.serverName));
    dispatch(setDatasourceColumns(columns));
    dispatch(initialize(formName, json));
  }
};
//used for widgets chart
export const getWidgetConfigChart = formName => (dispatch, getState) => {
  const state = getState();
  const config = getWidgetConfigSelector(state); // || CONFIG_CHART;
  if (config) {
    const configJson = JSON.parse(config.config);
    console.log("config", configJson);

    dispatch(fecthDatasourceList(configJson.serverName));

    // API ancora mockata
    getDatasourceData(
      configJson.serverName,
      configJson.datasource,
      DATASOURCE_PROPERTY_COLUMNS
    ).then(response => {
      response.json().then(json => {
        if (response.ok) {
          dispatch(initialize(formName, configJson));
          dispatch(clearSelectedDatasource());
          dispatch(setSelectedDatasource(configJson.datasource));
          // // API ancora mockata
          dispatch(
            setDatasourceColumns(
              json.payload.map(m => ({
                key: m.key,
                value: m.value
              }))
            )
          );

          configJson.columns.x &&
            configJson.columns.x.forEach((item, index) => {
              dispatch(arrayPush(formName, "columns.x", item));
            });

          configJson.columns.y &&
            configJson.columns.y.forEach(item => {
              dispatch(arrayPush(formName, "columns.y", item));
            });
          configJson.columns.y2 &&
            configJson.columns.y2.forEach(item => {
              dispatch(arrayPush(formName, "columns.y2", item));
            });
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText("plugin.alert.error"), TOAST_ERROR));
        }
      });
    });
  }
};

export const fetchLanguages = () => dispatch =>
  new Promise(resolve => {
    getLanguages().then(response => {
      response.json().then(json => {
        if (response.ok) {
          const languages = json.payload.filter(f => f.isActive);
          dispatch(setLanguages(languages));
          resolve();
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText("plugin.alert.error"), TOAST_ERROR));
          resolve();
        }
      });
    });
  });

export const fetchServerType = () => dispatch =>
  new Promise(resolve => {
    getServerType().then(response => {
      response.json().then(json => {
        if (response.ok) {
          dispatch(setServerType(json.payload));
          resolve();
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText("plugin.alert.error"), TOAST_ERROR));
          resolve();
        }
      });
    });
  }).catch(() => {});

export const fetchServerConfigList = configItem => dispatch =>
  new Promise(resolve => {
    getServerConfig(configItem).then(response => {
      response.json().then(json => {
        if (response.ok) {
          dispatch(setServerConfigList(json.payload));
          if (configItem) {
            dispatch(setInternalRoute("edit"));
          }
          resolve();
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText("plugin.alert.error"), TOAST_ERROR));
          resolve();
        }
      });
    });
  }).catch(() => {});

export const editServerConfig = (formName, configItem) => dispatch =>
  new Promise(resolve => {
    getDatasources(configItem.id).then(response => {
      response.json().then(json => {
        if (response.ok) {
          dispatch(setInternalRoute("edit"));
          const config = configItem;
          config.datasources = [...json.payload];
          dispatch(initialize(formName, config));
          resolve();
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText("plugin.alert.error"), TOAST_ERROR));
          resolve();
        }
      });
    });
  });

export const removeServerConfig = id => dispatch =>
  new Promise(resolve => {
    deleteServerConfig(id).then(response => {
      if (response.ok) {
        dispatch(removeServerConfigSync(id));
        resolve();
      } else {
        dispatch(addToast(formattedText("plugin.alert.error"), TOAST_ERROR));
        resolve();
      }
    });
  });

export const createServerConfig = serverConfig => dispatch =>
  new Promise(resolve => {
    postServerConfig(serverConfig).then(response => {
      response.json().then(json => {
        if (response.ok) {
          dispatch(setInternalRoute("home"));
          dispatch(addServerConfig(json.payload));
          resolve();
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText("plugin.alert.error"), TOAST_ERROR));
          resolve();
        }
      });
    });
  });

export const updateServerConfig = serverConfig => dispatch =>
  new Promise(resolve => {
    putServerConfig(serverConfig).then(response => {
      response.json().then(json => {
        if (response.ok) {
          dispatch(setInternalRoute("home"));
          dispatch(updateServerConfigAction(json.payload));
          resolve();
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText("plugin.alert.error"), TOAST_ERROR));
          resolve();
        }
      });
    });
  });

export const fecthDatasourceList = serverId => dispatch =>
  new Promise(resolve => {
    if (serverId) {
      getDatasources(serverId).then(response => {
        response.json().then(json => {
          if (response.ok) {
            dispatch(setDatasourceList(json.payload));
            resolve();
          } else {
            dispatch(addErrors(json.errors.map(e => e.message)));
            dispatch(
              addToast(formattedText("plugin.alert.error"), TOAST_ERROR)
            );
            resolve();
          }
        });
      });
    } else {
      dispatch(setDatasourceList([]));
      resolve();
    }
  });

const wrapApiCallFetchDatasource = (apiCall, actionCreator) => (
  ...args
) => dispatch =>
  new Promise(resolve => {
    apiCall(...args).then(response => {
      response.json().then(json => {
        if (response.ok) {
          console.log("json", json.payload);
          console.log("actionCreator", actionCreator);
          dispatch(actionCreator(json.payload));
          resolve(json.payload);
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText("plugin.alert.error"), TOAST_ERROR));
          resolve();
        }
      });
    });
  });

export const fetchDatasourceColumns = (formName, field, datasourceId) => (
  dispatch,
  getState
) =>
  new Promise(resolve => {
    const state = getState();
    const selector = formValueSelector(formName);
    const serverId = selector(state, field);
    dispatch(clearSelectedDatasource());
    dispatch(setSelectedDatasource(datasourceId));
    getDatasourceData(serverId, datasourceId, DATASOURCE_PROPERTY_COLUMNS).then(
      response => {
        response.json().then(json => {
          if (response.ok) {
            // API ancora mockata
            dispatch(setDatasourceColumns(json.payload));
            // set values in input field
            json.payload.forEach(item => {
              dispatch(
                change(
                  formName,
                  `${DATASOURCE_PROPERTY_COLUMNS}.${item.key}.label`,
                  item.value
                )
              );
            });
            resolve();
          } else {
            dispatch(addErrors(json.errors.map(e => e.message)));
            dispatch(
              addToast(formattedText("plugin.alert.error"), TOAST_ERROR)
            );
            resolve();
          }
        });
      }
    );
  });

export const fetchDatasourceData = (configId, datasourceId) => dispatch =>
  dispatch(
    wrapApiCallFetchDatasource(getDatasourceData, setDatasourceData)(
      configId,
      datasourceId,
      DATASOURCE_PROPERTY_DATA
    )
  );

export const updateDatasourceColumns = (formName, columns) => (
  dispatch,
  getState
) =>
  new Promise(resolve => {
    const state = getState();
    const selector = formValueSelector(formName);
    const configId = selector(state, "serverName");
    const datasourceId = selector(state, "datasource");
    putDatasourceColumn(configId, datasourceId, columns).then(response => {
      response.json().then(json => {
        if (response.ok) {
          dispatch(setDatasourceColumns(columns));
          resolve();
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText("plugin.alert.error"), TOAST_ERROR));
          resolve();
        }
      });
    });
  });

/*
 Widget Chart thunk
*/
