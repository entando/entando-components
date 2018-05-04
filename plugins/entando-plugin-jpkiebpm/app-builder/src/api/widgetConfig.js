import { makeRequest, METHODS } from '@entando/apimanager';

// eslint-disable-next-line
export const getDeploymentUnits = knowledgeSourceId => (
  makeRequest({
    uri: `/api/kiebpm/serverConfigs/${knowledgeSourceId}/deploymentUnits`,
    method: METHODS.GET,
    mockResponse: [],
    useAuthentication: true,
  })
);

export const getCaseDefinitions = (knowledgeSourceId, deploymentUnitId) => (
  makeRequest({
    uri: `/api/kiebpm/serverConfigs/${knowledgeSourceId}/caseDefinitions/${deploymentUnitId}`,
    method: METHODS.GET,
    mockResponse: [],
    useAuthentication: true,
  })
);
