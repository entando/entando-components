import reducer from "state/main/reducer";

import {
  SERVER_PIA,
  DASHBOARD_CONFIG_LIST,
  DASHBOARD_LIST_DATASOURCE,
  DATASOURCE_TEMPERATURE_DATA
} from "mocks/dashboardConfigs";

import {
  setServerConfigList,
  addServerConfig,
  updateServerConfigAction,
  removeServerConfigSync,
  setDatasourceList,
  setDatasourceColumns,
  clearDatasourceColumns,
  setDatasourceData,
  setSelectedDatasource,
  clearSelectedDatasource,
  setInternalRoute
} from "state/main/actions";

describe("state/main/reducer", () => {
  const state = reducer();
  let newState;

  it("should return an object", () => {
    expect(typeof state).toBe("object");
  });

  describe("internalRoute", () => {
    it("SET_INTERNAL_ROUTE", () => {
      newState = reducer(state, setInternalRoute("home"));
      expect(newState).toHaveProperty("internalRoute", "home");
    });
  });

  describe("servers", () => {
    it("SET_SERVER_CONFIG_LIST", () => {
      newState = reducer(state, setServerConfigList(DASHBOARD_CONFIG_LIST));
      expect(newState.dashboardConfig.servers instanceof Array).toBe(true);
      expect(newState.dashboardConfig.servers).toEqual(
        expect.arrayContaining(DASHBOARD_CONFIG_LIST)
      );
    });

    it("ADD_SERVER_CONFIG", () => {
      newState = reducer(state, addServerConfig(SERVER_PIA));
      expect(newState).toHaveProperty("dashboardConfig.servers");
      expect(newState.dashboardConfig.servers).toContainEqual(SERVER_PIA);
    });

    it("UPDATE_SERVER_CONFIG", () => {
      newState = reducer(state, addServerConfig(SERVER_PIA));
      newState = reducer(newState, updateServerConfigAction(SERVER_PIA));
      expect(newState).toHaveProperty("dashboardConfig.servers");
      expect(newState.dashboardConfig.servers).toContainEqual(SERVER_PIA);
    });

    it("REMOVE_SERVER_CONFIG", () => {
      newState = reducer(state, setServerConfigList(DASHBOARD_CONFIG_LIST));
      newState = reducer(newState, removeServerConfigSync("2"));
      expect(newState.dashboardConfig.servers).not.toContainEqual("2");
    });
  });

  describe("datasourceList", () => {
    it("SET_DATASOURCE_LIST", () => {
      newState = reducer(state, setDatasourceList(DASHBOARD_LIST_DATASOURCE));
      expect(newState).toHaveProperty(
        "dashboardConfig.datasourceList",
        DASHBOARD_LIST_DATASOURCE
      );
    });
  });

  describe("datasourceSelected", () => {
    it("SET_SELECTED_DATASOURCE", () => {
      newState = reducer(state, setSelectedDatasource("temperature"));
      expect(newState).toHaveProperty(
        "dashboardConfig.datasource.selected",
        "temperature"
      );
    });

    it("CLEAR_SELECTED_DATASOURCE", () => {
      newState = reducer(state, clearSelectedDatasource());
      expect(newState).toHaveProperty(
        "dashboardConfig.datasource.selected",
        ""
      );
    });
  });

  describe("datasourceColumns", () => {
    it("SET_DATASOURCE_COLUMNS", () => {
      newState = reducer(
        state,
        setDatasourceColumns(DATASOURCE_TEMPERATURE_DATA.columns)
      );
      expect(newState).toHaveProperty(
        "dashboardConfig.datasource.columns",
        DATASOURCE_TEMPERATURE_DATA.columns
      );
    });

    it("CLEAR_DATASOURCE_COLUMNS", () => {
      newState = reducer(state, clearDatasourceColumns());
      expect(newState).toHaveProperty("dashboardConfig.datasource.columns", []);
    });
  });

  describe("datasourceData", () => {
    it("SET_DATASOURCE_DATA", () => {
      newState = reducer(
        state,
        setDatasourceData(DATASOURCE_TEMPERATURE_DATA.data)
      );
      expect(newState).toHaveProperty(
        "dashboardConfig.datasource.data",
        DATASOURCE_TEMPERATURE_DATA.data
      );
    });
  });

  // end describe
});
