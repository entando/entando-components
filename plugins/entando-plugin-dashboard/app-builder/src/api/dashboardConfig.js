import {makeMockRequest, makeRequest, METHODS} from "@entando/apimanager";
import {
  DASHBOARD_CONFIG_LIST,
  DASHBOARD_LIST_DATASOURCE,
  DATASOURCE_TEMPERATURE_DATA
} from "mocks/dashboardConfigs";

export const getServerConfig = configItem => {
  let uri = "/api/plugins/dashboard/dashboardConfigs";
  let mockResponse = DASHBOARD_CONFIG_LIST;
  if (configItem) {
    uri = `${uri}/${configItem.id}`;
    mockResponse = DASHBOARD_CONFIG_LIST[configItem.id];
  }
  return makeRequest({
    uri,
    method: METHODS.GET,
    mockResponse,
    useAuthentication: true
  });
};

export const postServerConfig = serverConfig =>
  makeRequest({
    uri: "/api/plugins/dashboard/dashboardConfigs",
    method: METHODS.POST,
    body: serverConfig,
    mockResponse: {...serverConfig},
    useAuthentication: true
  });

export const putServerConfig = serverConfig =>
  makeRequest({
    uri: `/api/plugins/dashboard/dashboardConfigs/${serverConfig.id}`,
    method: METHODS.PUT,
    body: serverConfig,
    mockResponse: {...serverConfig},
    useAuthentication: true
  });

export const deleteServerConfig = serverConfigId =>
  makeRequest({
    uri: `/api/plugins/dashboard/dashboardConfigs/${serverConfigId}`,
    method: METHODS.DELETE,
    mockResponse: {},
    useAuthentication: true
  });

export const getDatasources = serverId =>
  makeRequest({
    uri: `/api/plugins/dashboard/dashboardConfigs/${serverId}/datasources`,
    method: METHODS.GET,
    mockResponse: DASHBOARD_LIST_DATASOURCE[serverId] || [],
    useAuthentication: true
  });

export const getDatasourceData = (
  serverId,
  datasourceId,
  type,
  page = {page: 1, pageSize: 0}
) =>
  makeMockRequest(
    {
      uri: `/api/plugins/dashboard/dashboardConfigs/${serverId}/datasource/${datasourceId}/${type}`,
      method: METHODS.GET,
      mockResponse: DATASOURCE_TEMPERATURE_DATA[type],
      useAuthentication: true
    },
    page
  );

export const putDatasourceColumn = (serverId, datasourceId, columns) =>
  makeMockRequest({
    uri: `/api/plugins/dashboard/dashboardConfigs/${serverId}/datasource/${datasourceId}/columns`,
    method: METHODS.PUT,
    body: columns,
    mockResponse: columns,
    useAuthentication: true
  });
