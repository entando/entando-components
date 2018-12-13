import {makeMockRequest, METHODS} from "@entando/apimanager";
import {
  SERVER_CONFIG_LIST,
  SERVER_CONFIG_LIST_CONTEXT
} from "mocks/serverConfigs";

export const getServerConfigs = () =>
  makeMockRequest({
    uri: "/api/dashboard/serverConfigs",
    method: METHODS.GET,
    mockResponse: SERVER_CONFIG_LIST,
    useAuthentication: false
  });

export const postServerConfig = serverConfig =>
  makeMockRequest({
    uri: "/api/dashboard/serverConfigs",
    method: METHODS.POST,
    body: serverConfig,
    mockResponse: {...serverConfig},
    useAuthentication: false
  });

export const putServerConfig = serverConfig =>
  makeMockRequest({
    uri: `/api/dashboard/serverConfigs/${serverConfig.id}`,
    method: METHODS.PUT,
    body: serverConfig,
    mockResponse: {...serverConfig},
    useAuthentication: false
  });

export const deleteServerConfig = serverConfigId =>
  makeMockRequest({
    uri: `/api/dashboard/serverConfigs/${serverConfigId}`,
    method: METHODS.DELETE,
    mockResponse: {},
    useAuthentication: false
  });

export const getContexts = configId =>
  makeMockRequest({
    uri: `/api/dashboard/serverConfig/${configId}/contexts`,
    method: METHODS.GET,
    mockResponse: SERVER_CONFIG_LIST_CONTEXT[configId].context,
    useAuthentication: false
  });

export const getContext = (configId, contextId) =>
  makeMockRequest({
    uri: `/api/dashboard/serverConfig/${configId}/context/${contextId}`,
    method: METHODS.GET,
    mockResponse: SERVER_CONFIG_LIST_CONTEXT[contextId],
    useAuthentication: false
  });
