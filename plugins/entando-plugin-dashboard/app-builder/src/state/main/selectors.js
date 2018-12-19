import {createSelector} from "reselect";
import {getSearchParams} from "@entando/router";
import {name as id} from "../../../package.json";

export const getLocalState = state => state.plugins[id];

export const getAppbuilder = createSelector(
  [getLocalState],
  localState => localState.appBuilder
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

export const getDatasourceColumns = createSelector(
  getLocalState,
  localState => localState.datasourceColumns
);

export const getPluginPage = createSelector(
  [getSearchParams],
  searchParams => searchParams.pluginPage || ""
);
