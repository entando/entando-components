import {makeMockRequest, METHODS} from "@entando/apimanager";
import {
  SERVER_CONFIG_LIST,
  SERVER_CONFIG_LIST_CONTEXT
} from "mocks/serverConfigs";

export const getServerConfigs = () =>
  makeMockRequest({
    uri: "/api/iot/serverConfigs",
    method: METHODS.GET,
    mockResponse: SERVER_CONFIG_LIST,
    useAuthentication: true
  });

export const postServerConfig = serverConfig =>
  makeMockRequest({
    uri: "/api/iot/serverConfigs",
    method: METHODS.POST,
    body: serverConfig,
    mockResponse: {...serverConfig},
    useAuthentication: true
  });

export const putServerConfig = serverConfig =>
  makeMockRequest({
    uri: `/api/iot/serverConfigs/${serverConfig.id}`,
    method: METHODS.PUT,
    body: serverConfig,
    mockResponse: {...serverConfig},
    useAuthentication: true
  });

export const deleteServerConfig = serverConfigId =>
  makeMockRequest({
    uri: `/api/iot/serverConfigs/${serverConfigId}`,
    method: METHODS.DELETE,
    mockResponse: {},
    useAuthentication: true
  });

export const getServerContexts = configId =>
  makeMockRequest({
    uri: `/api/iot/serverConfig/${configId}/contexts`,
    method: METHODS.GET,
    mockResponse: SERVER_CONFIG_LIST_CONTEXT,
    useAuthentication: true
  });

export const getServerContext = (configId, contextId) =>
  makeMockRequest({
    uri: `/api/iot/serverConfig/${configId}/context/${contextId}`,
    method: METHODS.GET,
    mockResponse: SERVER_CONFIG_LIST_CONTEXT[contextId],
    useAuthentication: true
  });
