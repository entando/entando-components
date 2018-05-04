import { getDeploymentUnits, getCaseDefinitions } from 'api/widgetConfig';

import { SET_DEPLOYMENT_UNITS_LIST, SET_CASE_DEFINITIONS_LIST } from './types';

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
