import { getRoute, getParams, getSearchParams, gotoRoute } from '@entando/router';
import { addToast, addErrors, TOAST_ERROR } from '@entando/messages';
import { formattedText } from '@entando/utils';
import { formValueSelector, initialize, change, arrayPush } from 'redux-form';
import { get } from 'lodash';

import { getLanguages } from 'api/appBuilder';

import {
  getServerType,
  getServerConfig,
  deleteServerConfig,
  postServerConfig,
  putServerConfig,
  pingServerConfig,
  pingDatasource,
  getDatasources,
  getDatasourceColumns,
  previewDatasource,
} from 'api/dashboardConfig';

import { getWidgetConfigSelector } from 'state/app-builder/selectors';

import { getServerConfigList } from 'state/main/selectors';

// import { CONFIG_CHART_STRING } from 'mocks/dashboardConfigs';

import {
  SET_INFO_PAGE,
  SET_LANGUAGES,
  SET_SERVER_CONFIG_LIST,
  SET_SERVER_TYPE,
  ADD_SERVER_CONFIG,
  UPDATE_SERVER_CONFIG,
  REMOVE_SERVER_CONFIG,
  SET_CHECK_SERVER,
  SET_CHECK_DATASOURCE,
  SET_PREVIEW_DATASOURCE,
  SET_DATASOURCE_LIST,
  SET_DATASOURCE_DATA,
  SET_DATASOURCE_COLUMNS,
  CLEAR_DATASOURCE_COLUMNS,
  SET_SELECTED_DATASOURCE,
  CLEAR_SELECTED_DATASOURCE,
  SET_INTERNAL_ROUTE,
} from './types';


export const setInfoPage = info => ({
  type: SET_INFO_PAGE,
  payload: {
    info,
  },
});

export const setLanguages = languages => ({
  type: SET_LANGUAGES,
  payload: {
    languages,
  },
});

export const setServerType = serverTypeList => ({
  type: SET_SERVER_TYPE,
  payload: {
    serverTypeList,
  },
});

export const setServerConfigList = serverList => ({
  type: SET_SERVER_CONFIG_LIST,
  payload: {
    serverList,
  },
});

export const addServerConfig = server => ({
  type: ADD_SERVER_CONFIG,
  payload: {
    server,
  },
});

export const setCheckServer = (serverId, status) => ({
  type: SET_CHECK_SERVER,
  payload: { serverId, status },
});

export const setCheckDatasource = (datasourceId, status) => ({
  type: SET_CHECK_DATASOURCE,
  payload: { datasourceId, status },
});

export const setPreviewDatasource = fields => ({
  type: SET_PREVIEW_DATASOURCE,
  payload: { fields },
});

export const updateServerConfigAction = server => ({
  type: UPDATE_SERVER_CONFIG,
  payload: {
    server,
  },
});

export const removeServerConfigSync = configId => ({
  type: REMOVE_SERVER_CONFIG,
  payload: {
    configId,
  },
});

export const setDatasourceList = datasourceList => ({
  type: SET_DATASOURCE_LIST,
  payload: {
    datasourceList,
  },
});

export const setDatasourceColumns = columns => ({
  type: SET_DATASOURCE_COLUMNS,
  payload: {
    columns,
  },
});

export const clearDatasourceColumns = () => ({
  type: CLEAR_DATASOURCE_COLUMNS,
});

export const setDatasourceData = data => ({
  type: SET_DATASOURCE_DATA,
  payload: {
    data,
  },
});

export const setSelectedDatasource = datasourceId => ({
  type: SET_SELECTED_DATASOURCE,
  payload: {
    datasourceId,
  },
});

export const clearSelectedDatasource = () => ({
  type: CLEAR_SELECTED_DATASOURCE,
});

export const setInternalRoute = route => ({
  type: SET_INTERNAL_ROUTE,
  payload: { route },
});

// thunk

export const gotoConfigurationPage = () => (dispatch, getState) => {
  const state = getState();
  const params = getParams(state);
  window.location.assign(`/page/configuration/${params.pageCode}`);
};

export const gotoPluginPage = pluginPage => (dispatch, getState) => {
  const state = getState();
  const route = getRoute(state);
  const params = getParams(state);

  const searchParams = getSearchParams(state);
  try {
    gotoRoute(route, params, { ...searchParams, pluginPage });
  } catch (error) {
    console.error('error', error);
    dispatch(setInternalRoute(pluginPage));
  }
};

export const fetchLanguages = () => dispatch =>
  new Promise((resolve) => {
    getLanguages().then((response) => {
      response.json().then((json) => {
        if (response.ok) {
          const languages = json.payload.filter(f => f.isActive);
          dispatch(setLanguages(languages));
          resolve();
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText('plugin.alert.error'), TOAST_ERROR));
          resolve();
        }
      });
    });
  });

export const fetchServerType = () => dispatch =>
  new Promise((resolve) => {
    getServerType().then((response) => {
      response.json().then((json) => {
        if (response.ok) {
          dispatch(setServerType(json.payload));
          resolve();
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText('plugin.alert.error'), TOAST_ERROR));
          resolve();
        }
      });
    });
  }).catch(() => {});

export const fetchServerConfigList = configItem => dispatch =>
  new Promise((resolve) => {
    getServerConfig(configItem).then((response) => {
      response.json().then((json) => {
        if (response.ok) {
          dispatch(setServerConfigList(json.payload));
          if (configItem) {
            dispatch(setInternalRoute('edit'));
          }
          resolve();
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText('plugin.alert.error'), TOAST_ERROR));
          resolve();
        }
      });
    });
  }).catch(() => {});

export const editServerConfig = (formName, configItem) => dispatch =>
  new Promise((resolve) => {
    getDatasources(configItem.id).then((response) => {
      response.json().then((json) => {
        if (response.ok) {
          dispatch(setInternalRoute('edit'));
          const config = configItem;
          config.datasources = [...json.payload];
          dispatch(initialize(formName, config));
          resolve();
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText('plugin.alert.error'), TOAST_ERROR));
          resolve();
        }
      });
    });
  });

export const removeServerConfig = id => dispatch =>
  new Promise((resolve) => {
    deleteServerConfig(id).then((response) => {
      if (response.ok) {
        dispatch(removeServerConfigSync(id));
        resolve();
      } else {
        dispatch(addToast(formattedText('plugin.alert.error'), TOAST_ERROR));
        resolve();
      }
    });
  });

export const createServerConfig = serverConfig => dispatch =>
  new Promise((resolve) => {
    postServerConfig(serverConfig).then((response) => {
      response.json().then((json) => {
        if (response.ok) {
          dispatch(setInternalRoute('home'));
          dispatch(addServerConfig(json.payload));
          resolve();
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText('plugin.alert.error'), TOAST_ERROR));
          resolve();
        }
      });
    });
  });

export const updateServerConfig = serverConfig => dispatch =>
  new Promise((resolve) => {
    putServerConfig(serverConfig).then((response) => {
      response.json().then((json) => {
        if (response.ok) {
          dispatch(setInternalRoute('home'));
          dispatch(updateServerConfigAction(json.payload));
          resolve();
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText('plugin.alert.error'), TOAST_ERROR));
          resolve();
        }
      });
    });
  });

export const checkStatusServerConfig = serverId => (dispatch, getState) =>
  new Promise((resolve) => {
    const state = getState();
    const serverList = getServerConfigList(state);
    if (serverId) {
      pingServerConfig(serverId).then((response) => {
        response.json().then((json) => {
          if (response.ok) {
            dispatch(setCheckServer(serverId, { status: 'online' }));
            resolve();
          } else {
            dispatch(setCheckServer(serverId, { status: 'offline' }));
            const { errors } = json;
            dispatch(addErrors(errors.map(e => e.message)));
            errors.map(e => dispatch(addToast(e.message, TOAST_ERROR)));
            resolve();
          }
        });
      });
    } else {
      serverList.map(server => dispatch(checkStatusServerConfig(server.id)));
    }
  });

export const checkStatusDatasource = datasourceId => (dispatch, getState) =>
  new Promise((resolve) => {
    const state = getState();
    const selector = formValueSelector('dashboard-config-form');
    const serverId = selector(state, 'id');
    if (datasourceId) {
      if (serverId) {
        pingDatasource(serverId, datasourceId).then((response) => {
          response.json().then((json) => {
            if (response.ok) {
              dispatch(setCheckDatasource(datasourceId, { status: 'online' }));
              resolve();
            } else {
              dispatch(setCheckDatasource(datasourceId, { status: 'offline' }));
              const { errors } = json;
              dispatch(addErrors(errors.map(e => e.message)));
              errors.map(e => dispatch(addToast(e.message, TOAST_ERROR)));
              resolve();
            }
          });
        });
      }
    } else {
      const datasourceList = selector(state, 'datasources');
      datasourceList.map(ds => dispatch(checkStatusDatasource(ds.datasourceCode)));
    }

    resolve();
  });

export const fetchPreviewDatasource = datasourceId => (dispatch, getState) =>
  new Promise((resolve) => {
    const state = getState();
    const selector = formValueSelector('dashboard-config-form');
    const serverId = selector(state, 'id');
    if (datasourceId) {
      if (serverId) {
        previewDatasource(serverId, datasourceId).then((response) => {
          response.json().then((json) => {
            if (response.ok) {
              dispatch(setPreviewDatasource(json.payload.fields));
              resolve();
            } else {
              dispatch(addErrors(json.errors.map(e => e.message)));
              dispatch(addToast(formattedText('plugin.alert.error'), TOAST_ERROR));
              resolve();
            }
          });
        });
      }
    }
    resolve();
  });


export const fecthDatasourceList = serverId => dispatch =>
  new Promise((resolve) => {
    if (serverId) {
      getDatasources(serverId).then((response) => {
        response.json().then((json) => {
          if (response.ok) {
            dispatch(setDatasourceList(json.payload));
            resolve();
          } else {
            dispatch(addErrors(json.errors.map(e => e.message)));
            dispatch(addToast(formattedText('plugin.alert.error'), TOAST_ERROR));
            resolve();
          }
        });
      });
    } else {
      dispatch(setDatasourceList([]));
      resolve();
    }
  });

export const fetchDatasourceColumns = (formName, field, datasourceId) => (
  dispatch,
  getState,
) =>
  new Promise((resolve) => {
    const state = getState();
    const selector = formValueSelector(formName);
    const serverId = selector(state, field);
    dispatch(clearSelectedDatasource());
    dispatch(setSelectedDatasource(datasourceId));
    getDatasourceColumns(serverId, datasourceId).then((response) => {
      response.json().then((json) => {
        if (response.ok) {
          const { mappings } = json.payload;
          const columns = mappings.reduce((acc, item) => {
            acc.push({
              key: item.sourceName,
              value: item.destinationName,
            });
            return acc;
          }, []);
          dispatch(setDatasourceColumns(columns));
          // set values in input field
          columns.forEach((item) => {
            dispatch(change(formName, `columns.${item.key}.label`, item.value));
          });
          resolve();
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText('plugin.alert.error'), TOAST_ERROR));
          resolve();
        }
      });
    });
  });

export const updateDatasourceColumns = columns => dispatch =>
  dispatch(setDatasourceColumns(columns));


/*
 Widget Chart thunk
*/

// used for widget table
export const getWidgetConfig = formName => (dispatch, getState) => {
  const state = getState();
  const config = getWidgetConfigSelector(state);
  if (config) {
    const json = JSON.parse(config.config);
    const columns = Object.keys(json.columns)
      .sort((a, b) => json.columns[a].order - json.columns[b].order)
      .reduce((acc, key) => {
        const obj = {
          key,
          value: get(json.columns, `${key}.label`),
          hidden: get(json.columns, `${key}.hidden`, false),
        };
        acc.push(obj);
        return acc;
      }, []);
    dispatch(fecthDatasourceList(json.serverName));
    dispatch(setDatasourceColumns(columns));
    dispatch(initialize(formName, json));
  }
};

// used for widgets chart
export const getWidgetConfigChart = formName => (dispatch, getState) => {
  const state = getState();
  const config = getWidgetConfigSelector(state); // || CONFIG_CHART_STRING;
  if (config) {
    const configJson = JSON.parse(config.config);
    dispatch(fecthDatasourceList(configJson.serverName));
    getDatasourceColumns(configJson.serverName, configJson.datasource).then((response) => {
      response.json().then((json) => {
        if (response.ok) {
          dispatch(initialize(formName, configJson));
          dispatch(clearSelectedDatasource());
          dispatch(setSelectedDatasource(configJson.datasource));
          const { mappings } = json.payload;
          dispatch(setDatasourceColumns(mappings.map(m => ({
            key: m.sourceName,
            value: m.destinationName,
          }))));

          if (configJson.columns && configJson.columns.x.length > 0) {
            configJson.columns.x.map(item =>
              dispatch(arrayPush(formName, 'columns.x', item)));
          }
          if (configJson.columns.y && configJson.columns.y.length > 0) {
            configJson.columns.y.map(item =>
              dispatch(arrayPush(formName, 'columns.y', item)));
          }
          if (configJson.columns.y2 && configJson.columns.y2.length > 0) {
            configJson.columns.y2.map(item =>
              dispatch(arrayPush(formName, 'columns.y2', item)));
          }
        } else {
          dispatch(addErrors(json.errors.map(e => e.message)));
          dispatch(addToast(formattedText('plugin.alert.error'), TOAST_ERROR));
        }
      });
    });
  }
};
