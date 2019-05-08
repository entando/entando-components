import { createSelector } from 'reselect';
import { getSearchParams } from '@entando/router';
import { name as id } from '../../../package.json';

export const getLocalState = state => state.plugins[id];

export const getPluginPage = createSelector(
  [getSearchParams],
  searchParams => searchParams.pluginPage || '',
);

export const getAppbuilder = createSelector(
  [getLocalState],
  localState => localState.appBuilder,
);

export const getLanguages = createSelector(
  [getAppbuilder],
  ab => ab.languages,
);

export const getReduxStatus = createSelector(
  [getLocalState],
  localState => localState.reduxStatus,
);

export const getInternalRoute = createSelector(
  [getLocalState],
  localState => localState.internalRoute,
);

const getDashboardConfig = createSelector(
  [getLocalState],
  localState => localState.dashboardConfig,
);

export const getServerType = createSelector(
  [getDashboardConfig],
  dc => dc.serverType,
);
const getDatasouce = createSelector(
  [getDashboardConfig],
  dc => dc.datasource,
);

export const getDatasoucePreview = createSelector(
  [getDatasouce],
  dc => dc.preview,
);

export const getDatasouceSelected = createSelector(
  [getDatasouce],
  ds => ds.selected,
);
export const getDatasourceColumns = createSelector(
  [getDatasouce],
  ds => ds.columns,
);
export const getDatasouceData = createSelector(
  [getDatasouce],
  ds => ds.data,
);

export const getServerConfigList = createSelector(
  [getDashboardConfig],
  dc => dc.servers,
);

export const getServerCheck = createSelector(
  [getDashboardConfig],
  dc => dc.serverCheck,
);

export const getDatasourceCheck = createSelector(
  [getDashboardConfig],
  dc => dc.datasourceCheck,
);


export const getDatasourceList = createSelector(
  [getDashboardConfig],
  dc => dc.datasourceList,
);
