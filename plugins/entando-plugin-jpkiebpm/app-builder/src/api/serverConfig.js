import { makeRequest, METHODS } from '@entando/apimanager';
import { SERVER_CONFIG_LIST } from 'mocks/serverConfigs';

export const getServerConfigs = () => (
  makeRequest({
    uri: '/api/kiebpm/serverConfigs',
    method: METHODS.GET,
    mockResponse: SERVER_CONFIG_LIST,
    useAuthentication: true,
  })
);

export const postServerConfig = serverConfig => (
  makeRequest({
    uri: '/api/kiebpm/serverConfigs',
    method: METHODS.POST,
    body: serverConfig,
    mockResponse: { ...serverConfig },
    useAuthentication: true,
  })
);

export const putServerConfig = serverConfig => (
  makeRequest({
    uri: `/api/kiebpm/serverConfigs/${serverConfig.id}`,
    method: METHODS.PUT,
    body: serverConfig,
    mockResponse: { ...serverConfig },
    useAuthentication: true,
  })
);

export const deleteServerConfig = serverConfigId => (
  makeRequest({
    uri: `/api/kiebpm/serverConfigs/${serverConfigId}`,
    method: METHODS.DELETE,
    mockResponse: {},
    useAuthentication: true,
  })
);

export const sendTestServerConfig = serverConfig => (
  makeRequest({
    uri: '/api/kiebpm/testServerConfigs',
    method: METHODS.POST,
    body: serverConfig,
    mockResponse: serverConfig,
    useAuthentication: true,
  })
);

export const sendTestAllServerConfigs = () => (
  makeRequest({
    uri: '/api/kiebpm/testAllServerConfigs',
    method: METHODS.POST,
    body: {},
    mockResponse: {},
    useAuthentication: true,
  })
);
