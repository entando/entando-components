import {createSelector} from "reselect";
import {getSearchParams} from "@entando/router";
import {name as id} from "../../../package.json";

export const getLocalState = state => state.plugins[id];

export const getAppbuilder = createSelector(
  [getLocalState],
  localState => localState.appBuilder
);

const getDatasouce = createSelector(
  [getLocalState],
  localState => localState.datasource
);

export const getDatasouceSelected = createSelector(
  [getDatasouce],
  ds => ds.selected
);
export const getDatasourceColumns = createSelector(
  [getDatasouce],
  ds => ds.columns
);
export const getDatasouceData = createSelector(
  [getDatasouce],
  ds => ds.data
);

export const getInfoPage = createSelector(
  [getAppbuilder],
  ab => ab.infoPage
);
export const getLanguages = createSelector(
  [getAppbuilder],
  ab => ab.languages
);

export const getReduxStatus = createSelector(
  [getLocalState],
  localState => localState.reduxStatus
);

export const getServerConfigList = createSelector(
  [getLocalState],
  localState => localState.dashboardConfig
);

export const getDatasourceList = createSelector(
  [getLocalState],
  localState => localState.datasourceList
);

export const getPluginPage = createSelector(
  [getSearchParams],
  searchParams => searchParams.pluginPage || ""
);
