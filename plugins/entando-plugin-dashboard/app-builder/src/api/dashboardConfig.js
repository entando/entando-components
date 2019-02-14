import {makeMockRequest, METHODS} from "@entando/apimanager";
import {
  DASHBOARD_CONFIG_LIST,
  DASHBOARD_LIST_DATASOURCE,
  DATASOURCES_DATA
} from "mocks/dashboardConfigs";

export const getServerConfig = () =>
  makeMockRequest({
    uri: "/plugins/dashboard/dashboardConfigs",
    method: METHODS.GET,
    mockResponse: DASHBOARD_CONFIG_LIST,
    useAuthentication: false
  });

export const postServerConfig = serverConfig =>
  makeMockRequest({
    uri: "/plugins/dashboard/dashboardConfigs",
    method: METHODS.POST,
    body: serverConfig,
    mockResponse: {...serverConfig},
    useAuthentication: false
  });

export const putServerConfig = serverConfig =>
  makeMockRequest({
    uri: `/plugins/dashboard/dashboardConfigs/${serverConfig.id}`,
    method: METHODS.PUT,
    body: serverConfig,
    mockResponse: {...serverConfig},
    useAuthentication: false
  });

export const deleteServerConfig = serverConfigId =>
  makeMockRequest({
    uri: `/plugins/dashboard/dashboardConfigs/${serverConfigId}`,
    method: METHODS.DELETE,
    mockResponse: {},
    useAuthentication: false
  });

export const getDatasources = configId =>
  makeMockRequest({
    uri: `/plugins/dashboard/dashboardConfigs/${configId}/datasources`,
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
      uri: `/plugins/dashboard/dashboardConfigs/${configId}/datasource/${datasourceId}/${type}`,
      method: METHODS.GET,
      mockResponse: DATASOURCES_DATA[configId][datasourceId][type],
      useAuthentication: false
    },
    page
  );

export const putDatasourceColumn = (configId, datasourceId, columns) =>
  makeMockRequest({
    uri: `/plugins/dashboard/dashboardConfigs/${configId}/datasource/${datasourceId}/columns`,
    method: METHODS.PUT,
    body: columns,
    mockResponse: columns,
    useAuthentication: false
  });
