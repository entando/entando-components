import {makeMockRequest, METHODS} from "@entando/apimanager";
import {
  DASHBOARD_CONFIG_LIST,
  DASHBOARD_LIST_DATASOURCE,
  DATASOURCES_DATA
} from "mocks/dashboardConfigs";

export const getServerConfig = () =>
  makeMockRequest({
    uri: "/api/plugin/dashboard/serverConfig",
    method: METHODS.GET,
    mockResponse: DASHBOARD_CONFIG_LIST,
    useAuthentication: false
  });

export const postServerConfig = serverConfig =>
  makeMockRequest({
    uri: "/api/plugin/dashboard/serverConfig",
    method: METHODS.POST,
    body: serverConfig,
    mockResponse: {...serverConfig},
    useAuthentication: false
  });

export const putServerConfig = serverConfig =>
  makeMockRequest({
    uri: `/api/plugin/dashboard/serverConfig/${serverConfig.id}`,
    method: METHODS.PUT,
    body: serverConfig,
    mockResponse: {...serverConfig},
    useAuthentication: false
  });

export const deleteServerConfig = serverConfigId =>
  makeMockRequest({
    uri: `/api/plugin/dashboard/serverConfig/${serverConfigId}`,
    method: METHODS.DELETE,
    mockResponse: {},
    useAuthentication: false
  });

export const getDatasources = configId =>
  makeMockRequest({
    uri: `/api/plugin/dashboard/serverConfig/${configId}/datasources`,
    method: METHODS.GET,
    mockResponse: DASHBOARD_LIST_DATASOURCE[configId],
    useAuthentication: false
  });

export const getDatasourceData = (
  configId,
  datasourceId,
  type,
  page = {page: 1, pageSize: 0}
) =>
  makeMockRequest(
    {
      uri: `/api/plugin/dashboard/serverConfig/${configId}/datasource/${datasourceId}/${type}`,
      method: METHODS.GET,
      mockResponse: DATASOURCES_DATA[configId][datasourceId][type],
      useAuthentication: false
    },
    page
  );

export const putDatasourceColumn = (configId, datasourceId, columns) =>
  makeMockRequest({
    uri: `/api/plugin/dashboard/serverConfig/${configId}/datasource/${datasourceId}/columns`,
    method: METHODS.PUT,
    body: columns,
    mockResponse: columns,
    useAuthentication: false
  });
