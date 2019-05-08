import { makeRequest, METHODS } from '@entando/apimanager';
import {
  DASHBOARD_CONFIG_LIST,
  DASHBOARD_LIST_DATASOURCE,
  DATASOURCES_DATA,
  SERVER_TYPE_LIST,
} from 'mocks/dashboardConfigs';

export const getServerType = () =>
  makeRequest({
    uri: '/api/plugins/dashboard/dashboardConfigs/servertypes',
    method: METHODS.GET,
    mockResponse: SERVER_TYPE_LIST,
    useAuthentication: true,
  });

export const getServerConfig = (configItem) => {
  let uri = '/api/plugins/dashboard/dashboardConfigs';
  let mockResponse = DASHBOARD_CONFIG_LIST;
  if (configItem) {
    uri = `${uri}/${configItem.id}`;
    mockResponse = DASHBOARD_CONFIG_LIST[configItem.id];
  }
  return makeRequest({
    uri,
    method: METHODS.GET,
    mockResponse,
    useAuthentication: true,
  });
};

export const postServerConfig = serverConfig =>
  makeRequest({
    uri: '/api/plugins/dashboard/dashboardConfigs',
    method: METHODS.POST,
    body: serverConfig,
    mockResponse: { ...serverConfig },
    useAuthentication: true,
  });

export const putServerConfig = serverConfig =>
  makeRequest({
    uri: `/api/plugins/dashboard/dashboardConfigs/${serverConfig.id}`,
    method: METHODS.PUT,
    body: serverConfig,
    mockResponse: { ...serverConfig },
    useAuthentication: true,
  });

export const deleteServerConfig = serverConfigId =>
  makeRequest({
    uri: `/api/plugins/dashboard/dashboardConfigs/${serverConfigId}`,
    method: METHODS.DELETE,
    mockResponse: {},
    useAuthentication: true,
  });


export const pingServerConfig = serverConfigId =>
  makeRequest({
    uri: `/api/plugins/dashboard/dashboardConfigs/server/${serverConfigId}/ping`,
    method: METHODS.GET,
    mockResponse: {},
    useAuthentication: true,
  });


export const getDatasources = serverId =>
  makeRequest({
    uri: `/api/plugins/dashboard/dashboardConfigs/${serverId}/datasources`,
    method: METHODS.GET,
    mockResponse: DASHBOARD_LIST_DATASOURCE[serverId] || [],
    useAuthentication: true,
  });

export const getDatasourceColumns = (
  serverId,
  datasourceId,
  page = { page: 1, pageSize: 0 },
) =>
  makeRequest(
    {
      uri: `/api/plugins/dashboard/dashboardConfigs/server/${serverId}/datasource/${datasourceId}/columns`,
      method: METHODS.GET,
      mockResponse: DATASOURCES_DATA[datasourceId] || {},
      useAuthentication: true,
    },
    page,
  );

export const putDatasourceColumn = (serverId, datasourceId, columns) =>
  makeRequest({
    uri: `/api/plugins/dashboard/dashboardConfigs/server/${serverId}/datasource/${datasourceId}/columns`,
    method: METHODS.PUT,
    body: columns,
    mockResponse: columns,
    useAuthentication: true,
  });

export const getDatasourceData = (
  serverId,
  datasourceId,
  page = { page: 1, pageSize: 0 },
) =>
  makeRequest(
    {
      uri: `/api/plugins/dashboard/server/${serverId}/datasource/${datasourceId}/data`,
      method: METHODS.GET,
      mockResponse: DATASOURCES_DATA[datasourceId].data,
      useAuthentication: true,
    },
    page,
  );

export const pingDatasource = (serverId, datasourceId) =>
  makeRequest({
    uri: `/api/plugins/dashboard/dashboardConfigs/server/${serverId}/datasource/${datasourceId}/ping`,
    method: METHODS.GET,
    mockResponse: {},
    useAuthentication: true,
  });

export const previewDatasource = (serverId, datasourceId) =>
  makeRequest({
    uri: `/api/plugins/dashboard/dashboardConfigs/server/${serverId}/datasource/${datasourceId}/preview`,
    method: METHODS.GET,
    mockResponse: {},
    useAuthentication: true,
  });

export const refreshDatasourceMetadata = (serverId, datasourceCode) =>
  makeRequest({
    uri: `/api/plugins/dashboard/dashboardConfigs/server/${serverId}/datasource/${datasourceCode}/refreshMetadata`,
    method: METHODS.POST,
    body: {
      serverId,
      datasourceCode,
    },
    mockResponse: {},
    useAuthentication: true,
  });
