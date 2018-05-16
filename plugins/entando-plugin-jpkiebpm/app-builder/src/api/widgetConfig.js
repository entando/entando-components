import { makeRequest, METHODS } from '@entando/apimanager';
import { CASE_DEFINITION_LIST } from 'mocks/caseDefinitions';
import { DEPLOYMENT_UNITS_LIST } from 'mocks/deploymentUnits';


export const getDeploymentUnits = knowledgeSourceId => (
  makeRequest({
    uri: `/api/kiebpm/serverConfigs/${knowledgeSourceId}/deploymentUnits`,
    method: METHODS.GET,
    mockResponse: DEPLOYMENT_UNITS_LIST,
    useAuthentication: true,
  })
);

export const getCaseDefinitions = (knowledgeSourceId, deploymentUnitId) => (
  makeRequest({
    uri: `/api/kiebpm/serverConfigs/${knowledgeSourceId}/caseDefinitions/${deploymentUnitId}`,
    method: METHODS.GET,
    mockResponse: CASE_DEFINITION_LIST,
    useAuthentication: true,
  })
);
