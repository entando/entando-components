import reducer from "state/main/reducer";

import {
  SERVER_PIA,
  DASHBOARD_CONFIG_LIST,
  DASHBOARD_LIST_DATASOURCE,
  DATASOURCE_TEMPERATURE_DATA
} from "mocks/dashboardConfigs";

import {
  setInfoPage,
  setLanguages,
  setServerConfigList,
  addServerConfig,
  removeServerConfigSync,
  setDatasourceList,
  setDatasourceColumns,
  clearDatasourceColumns,
  setDatasourceData,
  setSelectedDatasource,
  clearSelectedDatasource,
  setInternalRoute
} from "state/main/actions";

import {
  SET_INFO_PAGE,
  SET_LANGUAGES,
  SET_SERVER_CONFIG_LIST,
  ADD_SERVER_CONFIG,
  REMOVE_SERVER_CONFIG,
  SET_DATASOURCE_LIST,
  SET_DATASOURCE_COLUMNS,
  CLEAR_DATASOURCE_COLUMNS,
  SET_DATASOURCE_DATA,
  SET_SELECTED_DATASOURCE,
  CLEAR_SELECTED_DATASOURCE,
  SET_INTERNAL_ROUTE
} from "state/main/types";

describe("state/main/reducer", () => {
  const state = reducer();
  it("should return an object", () => {
    expect(typeof state).toBe("object");
  });

  describe("servers", () => {
    let newState;

    it("SET_SERVER_CONFIG_LIST", () => {
      newState = reducer(state, setServerConfigList(DASHBOARD_CONFIG_LIST));
      expect(newState.dashboardConfig.servers instanceof Array).toBe(true);
      expect(newState.dashboardConfig.servers).toEqual(
        expect.arrayContaining(DASHBOARD_CONFIG_LIST)
      );
    });

    it("ADD_SERVER_CONFIG", () => {
      newState = reducer(state, addServerConfig(SERVER_PIA));
      expect(newState.dashboardConfig.servers).toContainEqual(SERVER_PIA);
    });

    it("REMOVE_SERVER_CONFIG", () => {
      newState = reducer(state, setServerConfigList(DASHBOARD_CONFIG_LIST));
      newState = reducer(newState, removeServerConfigSync("2"));
      expect(newState.dashboardConfig.servers).not.toContainEqual("2");
    });
  });
});
