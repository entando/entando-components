import { makeRequest, makeMockRequest, METHODS } from '@entando/apimanager';
import { CASE_DEFINITION_LIST } from 'mocks/caseDefinitions';
import { DEPLOYMENT_UNITS_LIST } from 'mocks/deploymentUnits';
import { PROCESS_LIST } from 'mocks/processList';
import { OVERRIDES } from 'mocks/overrides';


export const getDeploymentUnits = knowledgeSourceId => (
  makeRequest({
    uri: `/api/iot/serverConfigs/${knowledgeSourceId}/deploymentUnits`,
    method: METHODS.GET,
    mockResponse: DEPLOYMENT_UNITS_LIST,
    useAuthentication: true,
  })
);

export const getCaseDefinitions = (knowledgeSourceId, deploymentUnitId) => (
  makeRequest({
    uri: `/api/iot/serverConfigs/${knowledgeSourceId}/caseDefinitions/${deploymentUnitId}`,
    method: METHODS.GET,
    mockResponse: CASE_DEFINITION_LIST,
    useAuthentication: true,
  })
);

export const getProcessList = knowledgeSourceId => (
  makeRequest({
    uri: `/api/iot/serverConfigs/${knowledgeSourceId}/processList`,
    method: METHODS.GET,
    mockResponse: PROCESS_LIST,
    useAuthentication: true,
  })
);

export const getOverrides = (knowledgeSourceId, processId) => (
  makeMockRequest({
    uri: `/api/iot/serverConfigs/${knowledgeSourceId}/processList/${processId}/overrides`,
    method: METHODS.GET,
    mockResponse: OVERRIDES,
    useAuthentication: true,
  })
);
