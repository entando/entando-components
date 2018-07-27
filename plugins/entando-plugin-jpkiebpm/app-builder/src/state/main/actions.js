import { getFormValues } from 'redux-form';
import { getRoute, getParams, getSearchParams, gotoRoute } from '@entando/router';

import {
  getServerConfigs, deleteServerConfig, sendTestServerConfig, sendTestAllServerConfigs,
  postServerConfig, putServerConfig,
} from 'api/serverConfig';
import {
  SET_SERVER_CONFIG_LIST, REMOVE_SERVER_CONFIG, ADD_CONNECTION_OUTCOMES, CLEAN_CONNECTION_OUTCOMES,
} from './types';

export const setServerConfigList = configList => ({
  type: SET_SERVER_CONFIG_LIST,
  payload: {
    configList,
  },
});

export const removeServerConfigSync = configId => ({
  type: REMOVE_SERVER_CONFIG,
  payload: {
    configId,
  },
});

export const addConnectionOutcomes = outcomes => ({
  type: ADD_CONNECTION_OUTCOMES,
  payload: {
    outcomes,
  },
});

export const cleanConnectionOutcomes = () => ({
  type: CLEAN_CONNECTION_OUTCOMES,
});

export const gotoPluginPage = pluginPage => (dispatch, getState) => {
  const state = getState();
  const route = getRoute(state);
  const params = getParams(state);
  const searchParams = getSearchParams(state);
  gotoRoute(route, params, { ...searchParams, pluginPage });
};

export const fetchServerConfigList = () => dispatch =>
  new Promise((resolve) => {
    getServerConfigs().then((response) => {
      if (response.ok) {
        response.json().then((json) => {
          dispatch(setServerConfigList(json.payload));
          resolve();
        });
      } else {
        resolve();
      }
    });
  });


export const removeServerConfig = id => dispatch =>
  new Promise((resolve) => {
    deleteServerConfig(id).then((response) => {
      if (response.ok) {
        dispatch(removeServerConfigSync(id));
      }
      resolve();
    });
  });

export const createServerConfig = serverConfig => dispatch =>
  new Promise((resolve) => {
    postServerConfig(serverConfig).then((response) => {
      if (response.ok) {
        fetchServerConfigList()(dispatch).then(resolve);
      } else {
        resolve();
      }
      resolve();
    });
  });

export const updateServerConfig = serverConfig => dispatch =>
  new Promise((resolve) => {
    putServerConfig(serverConfig).then((response) => {
      if (response.ok) {
        fetchServerConfigList()(dispatch).then(resolve);
      } else {
        resolve();
      }
    });
  });

export const testServerConfig = serverConfig => dispatch =>
  new Promise((resolve) => {
    sendTestServerConfig(serverConfig).then((response) => {
      if (response.ok) {
        response.json().then((json) => {
          dispatch(addConnectionOutcomes({
            [serverConfig.id]: json.payload.result,
          }));
          resolve();
        });
      } else {
        resolve();
      }
    });
  });

export const testFormServerConfig = () => (dispatch, getState) => {
  const formValues = getFormValues('jpkiebpm_serverConfig')(getState());
  formValues.port = parseInt(formValues.port, 10);
  formValues.timeout = parseInt(formValues.timeout, 10);
  formValues.id = '@@form';
  return testServerConfig(formValues)(dispatch, getState);
};


export const testAllServerConfigs = () => dispatch =>
  new Promise((resolve) => {
    sendTestAllServerConfigs().then((response) => {
      if (response.ok) {
        response.json().then((json) => {
          dispatch(addConnectionOutcomes(json.payload));
          resolve();
        });
      } else {
        resolve();
      }
    });
  });
