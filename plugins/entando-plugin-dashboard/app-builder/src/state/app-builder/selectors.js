import {createSelector} from "reselect";
import {getParams} from "@entando/router";
import {get} from "lodash";

const getPagesState = state => state.pages;
const getConfigMap = state => get(state, "pageConfig.configMap", null);

const getSelectedPageConfig = createSelector(
  [getConfigMap, getParams],
  (configMap, params) => get(configMap, `${params.pageCode}`, null)
);

export const getWidgetConfigSelector = createSelector(
  [getSelectedPageConfig, getParams],
  (selectedPage, params) => get(selectedPage, `${params.framePos}.config`, null)
);

export const getInfoPage = createSelector(
  [getPagesState],
  pages => {
    return pages && (pages.selected || {});
  }
);
