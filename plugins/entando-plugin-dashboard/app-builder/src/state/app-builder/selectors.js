import {createSelector} from "reselect";
import {getParams} from "@entando/router";
import {get, set} from "lodash";

const configPropertyToJson = (config, property, nestedProperty, type) => {
  const obj = type === "array" ? [] : {};
  return Object.keys(config)
    .filter(f => f.includes(property))
    .reduce((acc, item) => {
      const data = item.split(".");
      const el = data[1] && data[1].split(":");
      if (el) {
        if (type === "array") {
          acc.push({key: el[0], value: config[item]});
        } else {
          if (nestedProperty) {
            set(acc, `${el[0]}.${nestedProperty}`, config[item]);
          } else {
            acc = {...acc, [el[0]]: config[item]};
          }
        }
      }
      return acc;
    }, obj);
};

const getPagesState = state => state.pages;
const getConfigMap = state => get(state, "pageConfig.configMap", null);

const getSelectedPageConfig = createSelector(
  [getConfigMap, getParams],
  (configMap, params) => get(configMap, `${params.pageCode}`, null)
);

export const getWidgetConfigSelector = createSelector(
  [getSelectedPageConfig, getParams],
  (selectedPage, params) => {
    const config = get(selectedPage, `${params.framePos}.config`);
    if (config) {
      config.columnsArray = configPropertyToJson(
        config,
        "columns",
        null,
        "array"
      );
      config.options = configPropertyToJson(config, "options", null, "object");
      config.title = configPropertyToJson(config, "title", null, "object");
      config.columns = configPropertyToJson(
        config,
        "columns",
        "label",
        "object"
      );

      return config;
    }
  }
);

export const getInfoPage = createSelector(
  [getPagesState],
  pages => {
    return pages && (pages.selected || {});
  }
);
