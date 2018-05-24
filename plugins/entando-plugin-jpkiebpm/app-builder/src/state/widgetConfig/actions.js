import { getDeploymentUnits, getCaseDefinitions, getProcessList, getOverrides } from 'api/widgetConfig';

import { SET_DEPLOYMENT_UNITS_LIST, SET_CASE_DEFINITIONS_LIST, SET_PROCESS_LIST, SET_OVERRIDES } from './types';

export const setDeploymentUnitsList = list => ({
  type: SET_DEPLOYMENT_UNITS_LIST,
  payload: {
    list,
  },
});

export const setCaseDefinitionsList = list => ({
  type: SET_CASE_DEFINITIONS_LIST,
  payload: {
    list,
  },
});

export const setProcessList = list => ({
  type: SET_PROCESS_LIST,
  payload: {
    list,
  },
});

export const setOverrides = list => ({
  type: SET_OVERRIDES,
  payload: {
    list,
  },
});


export const fetchDeploymentUnits = knowledgeSourceId => dispatch =>
  new Promise((resolve) => {
    dispatch(setDeploymentUnitsList([]));
    dispatch(setCaseDefinitionsList([]));
    getDeploymentUnits(knowledgeSourceId).then((response) => {
      if (response.ok) {
        response.json().then((json) => {
          dispatch(setDeploymentUnitsList(json.payload));
          resolve();
        });
      } else {
        resolve();
      }
    });
  });

export const fetchCaseDefinitions = (knowledgeSourceId, deploymentUnitId) => dispatch =>
  new Promise((resolve) => {
    dispatch(setCaseDefinitionsList([]));
    getCaseDefinitions(knowledgeSourceId, deploymentUnitId).then((response) => {
      if (response.ok) {
        response.json().then((json) => {
          dispatch(setCaseDefinitionsList(json.payload.definitions || []));
          resolve();
        });
      } else {
        resolve();
      }
    });
  });

export const fetchProcessList = knowledgeSourceId => dispatch =>
  new Promise((resolve) => {
    getProcessList(knowledgeSourceId).then((response) => {
      if (response.ok) {
        response.json().then((json) => {
          dispatch(setProcessList(json.payload));
          resolve();
        });
      } else {
        resolve();
      }
    });
  });

export const fetchOverrides = (knowledgeSourceId, processId) => dispatch =>
  new Promise((resolve) => {
    dispatch(setOverrides([]));
    getOverrides(knowledgeSourceId, processId).then((response) => {
      if (response.ok) {
        response.json().then((json) => {
          dispatch(setOverrides(json.payload));
          resolve();
        });
      } else {
        resolve();
      }
    });
  });
