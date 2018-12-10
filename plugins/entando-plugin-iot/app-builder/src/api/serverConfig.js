import { makeMockRequest, METHODS } from '@entando/apimanager';
import { SERVER_CONFIG_LIST } from 'mocks/serverConfigs';

export const getServerConfigs = () => (
  makeMockRequest({
    uri: '/api/iot/serverConfigs',
    method: METHODS.GET,
    mockResponse: SERVER_CONFIG_LIST,
    useAuthentication: true,
  })
);

export const postServerConfig = serverConfig => (
  makeMockRequest({
    uri: '/api/iot/serverConfigs',
    method: METHODS.POST,
    body: serverConfig,
    mockResponse: { ...serverConfig },
    useAuthentication: true,
  })
);

export const putServerConfig = serverConfig => (
  makeMockRequest({
    uri: `/api/iot/serverConfigs/${serverConfig.id}`,
    method: METHODS.PUT,
    body: serverConfig,
    mockResponse: { ...serverConfig },
    useAuthentication: true,
  })
);

export const deleteServerConfig = serverConfigId => (
  makeMockRequest({
    uri: `/api/iot/serverConfigs/${serverConfigId}`,
    method: METHODS.DELETE,
    mockResponse: {},
    useAuthentication: true,
  })
);

export const sendTestServerConfig = serverConfig => (
  makeMockRequest({
    uri: '/api/iot/testServerConfigs',
    method: METHODS.POST,
    body: serverConfig,
    mockResponse: serverConfig,
    useAuthentication: true,
  })
);

export const sendTestAllServerConfigs = () => (
  makeMockRequest({
    uri: '/api/iot/testAllServerConfigs',
    method: METHODS.POST,
    body: {},
    mockResponse: {},
    useAuthentication: true,
  })
);
