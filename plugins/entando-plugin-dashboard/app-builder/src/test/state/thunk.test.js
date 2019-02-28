import configureMockStore from "redux-mock-store";
import thunk from "redux-thunk";
import {initialize} from "redux-form";
import {ADD_ERRORS, ADD_TOAST} from "@entando/messages";

import {mockApi} from "../testUtils";

import {getLanguages} from "api/appBuilder";
import {
  getServerConfig,
  deleteServerConfig,
  postServerConfig,
  putServerConfig,
  getDatasources,
  getDatasourceData,
  putDatasourceColumn
} from "api/dashboardConfig";

import {
  SET_DATASOURCE_LIST,
  SET_DATASOURCE_COLUMNS,
  SET_LANGUAGES,
  SET_SERVER_CONFIG_LIST,
  SET_INTERNAL_ROUTE,
  REMOVE_SERVER_CONFIG,
  ADD_SERVER_CONFIG,
  UPDATE_SERVER_CONFIG,
  CLEAR_SELECTED_DATASOURCE,
  SET_SELECTED_DATASOURCE
} from "state/main/types";

import {
  getTableWidgetConfig,
  fetchLanguages,
  fetchServerConfigList,
  editServerConfig,
  removeServerConfig,
  createServerConfig,
  updateServerConfig,
  fecthDatasourceList,
  fetchDatasourceColumns,
  updateDatasourceColumns
} from "state/main/actions";

import {getWidgetConfigSelector} from "state/app-builder/selectors";

import {LANGUAGES} from "mocks/appBuilder";
import {
  DASHBOARD_CONFIG_LIST,
  DATASOURCE_TEMPERATURE,
  SERVER_PIA,
  DATASOURCE_TEMPERATURE_DATA
} from "mocks/dashboardConfigs";

const middlewares = [thunk];
const mockStore = configureMockStore(middlewares);

const INITIAL_STATE = {
  "entando-plugin-dashboard": {
    internalRoute: "",
    appBuilder: {
      infoPage: {},
      languages: {}
    },
    dashboardConfig: {
      servers: "",
      datasourceList: [],
      datasource: {
        selected: "",
        columns: {},
        data: []
      }
    }
  }
};

jest.mock("api/appBuilder");
jest.mock("api/dashboardConfig");

jest.mock("state/app-builder/selectors");
getWidgetConfigSelector.mockReturnValue({});

describe("state/main/actions", () => {
  let store;
  let actions;

  beforeEach(() => {
    store = mockStore(INITIAL_STATE);
  });

  describe("thunk", () => {
    describe("getTableWidgetConfig", () => {
      it("if exists a config in the widget table initialize the form", () => {
        store.dispatch(getTableWidgetConfig("form-dashboard-table"));
        actions = store.getActions();
        expect(actions).toHaveLength(3);
        expect(actions[0]).toHaveProperty("type", SET_DATASOURCE_LIST);
        expect(actions[1]).toHaveProperty("type", SET_DATASOURCE_COLUMNS);
        expect(actions[2]).toHaveProperty("type", "@@redux-form/INITIALIZE");
        expect(initialize).toHaveBeenCalled();
      });
    });

    describe("fetchLanguages", () => {
      beforeEach(() => {
        getLanguages.mockImplementation(mockApi({payload: LANGUAGES}));
      });
      it("if response OK dispatch setLanguages", done => {
        store.dispatch(fetchLanguages()).then(() => {
          actions = store.getActions();
          expect(actions).toHaveLength(1);
          expect(actions[0]).toHaveProperty("type", SET_LANGUAGES);
          done();
        });
      });
      it("if API response is not ok, dispatch ADD_ERRORS", done => {
        getLanguages.mockImplementation(mockApi({errors: true}));
        store
          .dispatch(fetchLanguages())
          .then(() => {
            expect(store.getActions()).toHaveLength(2);
            expect(store.getActions()[0]).toHaveProperty("type", ADD_ERRORS);
            expect(store.getActions()[1]).toHaveProperty("type", ADD_TOAST);
            done();
          })
          .catch(done.fail);
      });
    });

    describe("fetchServerConfigList", () => {
      beforeEach(() => {
        getServerConfig.mockImplementation(
          mockApi({payload: DASHBOARD_CONFIG_LIST})
        );
      });

      it("if response OK dispatch setServerConfigList", done => {
        store.dispatch(fetchServerConfigList()).then(() => {
          actions = store.getActions();
          expect(actions).toHaveLength(1);
          expect(actions[0]).toHaveProperty("type", SET_SERVER_CONFIG_LIST);
          done();
        });
      });

      it("if thunk have a set on configItem parameter dispatch setServerConfigList and setInternalRoute", done => {
        store
          .dispatch(fetchServerConfigList(DASHBOARD_CONFIG_LIST))
          .then(() => {
            expect(store.getActions()).toHaveLength(2);
            expect(store.getActions()[0]).toHaveProperty(
              "type",
              SET_SERVER_CONFIG_LIST
            );
            expect(store.getActions()[1]).toHaveProperty(
              "type",
              SET_INTERNAL_ROUTE
            );
            done();
          })
          .catch(done.fail);
      });

      it("if API response is not ok, dispatch ADD_ERRORS", done => {
        getServerConfig.mockImplementation(mockApi({errors: true}));
        store
          .dispatch(fetchServerConfigList(DASHBOARD_CONFIG_LIST))
          .then(() => {
            expect(store.getActions()).toHaveLength(2);
            expect(store.getActions()[0]).toHaveProperty("type", ADD_ERRORS);
            expect(store.getActions()[1]).toHaveProperty("type", ADD_TOAST);
            done();
          })
          .catch(done.fail);
      });
    });

    describe("editServerConfig", () => {
      beforeEach(() => {
        getDatasources.mockImplementation(
          mockApi({payload: [DATASOURCE_TEMPERATURE]})
        );
      });
      it("if response OK dispatch setInternalRoute and initialize", done => {
        store
          .dispatch(
            editServerConfig("form-datatable", DASHBOARD_CONFIG_LIST[0])
          )
          .then(() => {
            actions = store.getActions();
            expect(actions).toHaveLength(2);
            expect(actions[0]).toHaveProperty("type", SET_INTERNAL_ROUTE);
            expect(actions[1]).toHaveProperty(
              "type",
              "@@redux-form/INITIALIZE"
            );
            done();
          });
      });

      it("if API response is not ok, dispatch ADD_ERRORS", done => {
        getDatasources.mockImplementation(mockApi({errors: true}));
        store
          .dispatch(
            editServerConfig("form-datatable", DASHBOARD_CONFIG_LIST[0])
          )
          .then(() => {
            expect(store.getActions()).toHaveLength(2);
            expect(store.getActions()[0]).toHaveProperty("type", ADD_ERRORS);
            expect(store.getActions()[1]).toHaveProperty("type", ADD_TOAST);
            done();
          })
          .catch(done.fail);
      });
    });

    describe("removeServerConfig", () => {
      beforeEach(() => {
        deleteServerConfig.mockImplementation(mockApi({payload: {}}));
      });

      it("if response OK dispatch setInternalRoute and initialize", done => {
        store.dispatch(removeServerConfig("1")).then(() => {
          actions = store.getActions();
          expect(actions).toHaveLength(1);
          expect(actions[0]).toHaveProperty("type", REMOVE_SERVER_CONFIG);
          done();
        });
      });

      it("if API response is not ok, dispatch ADD_ERRORS", done => {
        deleteServerConfig.mockImplementation(mockApi({errors: true}));
        store
          .dispatch(removeServerConfig("1"))
          .then(() => {
            expect(store.getActions()).toHaveLength(1);
            expect(store.getActions()[0]).toHaveProperty("type", ADD_TOAST);
            done();
          })
          .catch(done.fail);
      });
    });

    describe("createServerConfig", () => {
      beforeEach(() => {
        postServerConfig.mockImplementation(mockApi({payload: SERVER_PIA}));
      });
      it("if response OK dispatch setInternalRoute and addServerConfig", done => {
        store.dispatch(createServerConfig(SERVER_PIA)).then(() => {
          actions = store.getActions();
          expect(actions).toHaveLength(2);
          expect(actions[0]).toHaveProperty("type", SET_INTERNAL_ROUTE);
          expect(actions[1]).toHaveProperty("type", ADD_SERVER_CONFIG);
          done();
        });
      });

      it("if API response is not ok, dispatch ADD_ERRORS", done => {
        postServerConfig.mockImplementation(mockApi({errors: true}));
        store
          .dispatch(createServerConfig(SERVER_PIA))
          .then(() => {
            expect(store.getActions()).toHaveLength(2);
            expect(store.getActions()[0]).toHaveProperty("type", ADD_ERRORS);
            expect(store.getActions()[1]).toHaveProperty("type", ADD_TOAST);
            done();
          })
          .catch(done.fail);
      });
    });

    describe("updateServerConfig", () => {
      beforeEach(() => {
        putServerConfig.mockImplementation(mockApi({payload: SERVER_PIA}));
      });
      it("if response OK dispatch setInternalRoute and setServerConfigList", done => {
        store.dispatch(updateServerConfig(SERVER_PIA)).then(() => {
          actions = store.getActions();
          expect(actions).toHaveLength(2);
          expect(actions[0]).toHaveProperty("type", SET_INTERNAL_ROUTE);
          expect(actions[1]).toHaveProperty("type", UPDATE_SERVER_CONFIG);
          done();
        });
      });

      it("if API response is not ok, dispatch ADD_ERRORS", done => {
        putServerConfig.mockImplementation(mockApi({errors: true}));
        store
          .dispatch(updateServerConfig(SERVER_PIA))
          .then(() => {
            expect(store.getActions()).toHaveLength(2);
            expect(store.getActions()[0]).toHaveProperty("type", ADD_ERRORS);
            expect(store.getActions()[1]).toHaveProperty("type", ADD_TOAST);
            done();
          })
          .catch(done.fail);
      });
    });

    describe("fecthDatasourceList", () => {
      beforeEach(() => {
        getDatasources.mockImplementation(
          mockApi({payload: [DATASOURCE_TEMPERATURE]})
        );
      });
      it("if response OK dispatch setDatasourceList", done => {
        store.dispatch(fecthDatasourceList("1")).then(() => {
          actions = store.getActions();
          expect(actions).toHaveLength(1);
          expect(actions[0]).toHaveProperty("type", SET_DATASOURCE_LIST);
          done();
        });
      });

      it("if API response is not ok, dispatch ADD_ERRORS", done => {
        getDatasources.mockImplementation(mockApi({errors: true}));
        store
          .dispatch(fecthDatasourceList("1"))
          .then(() => {
            expect(store.getActions()).toHaveLength(2);
            expect(store.getActions()[0]).toHaveProperty("type", ADD_ERRORS);
            expect(store.getActions()[1]).toHaveProperty("type", ADD_TOAST);
            done();
          })
          .catch(done.fail);
      });
    });

    describe("fetchDatasourceColumns", () => {
      beforeEach(() => {
        getDatasourceData.mockImplementation(
          mockApi({payload: DATASOURCE_TEMPERATURE_DATA.columns})
        );
      });
      it("if response OK dispatch clearSelectedDatasource, setSelectedDatasource", done => {
        store
          .dispatch(
            fetchDatasourceColumns("form-table", "columns", "temperature")
          )
          .then(() => {
            actions = store.getActions();
            expect(store.getActions()).toHaveLength(5);
            expect(actions[0]).toHaveProperty(
              "type",
              CLEAR_SELECTED_DATASOURCE
            );
            expect(actions[1]).toHaveProperty("type", SET_SELECTED_DATASOURCE);
            expect(actions[2]).toHaveProperty("type", SET_DATASOURCE_COLUMNS);
            expect(actions[3]).toHaveProperty("type", "@@redux-form/CHANGE");
            expect(actions[4]).toHaveProperty("type", "@@redux-form/CHANGE");
            done();
          })
          .catch(done.fail);
      });

      it("if API response is not ok, dispatch ADD_ERRORS", done => {
        getDatasourceData.mockImplementation(mockApi({errors: true}));
        store
          .dispatch(
            fetchDatasourceColumns("form-table", "columns", "temperature")
          )
          .then(() => {
            expect(store.getActions()).toHaveLength(4);
            expect(store.getActions()[2]).toHaveProperty("type", ADD_ERRORS);
            expect(store.getActions()[3]).toHaveProperty("type", ADD_TOAST);
            done();
          })
          .catch(done.fail);
      });
    });

    describe("updateDatasourceColumns", () => {
      beforeEach(() => {
        putDatasourceColumn.mockImplementation(
          mockApi({payload: DATASOURCE_TEMPERATURE_DATA.columns})
        );
      });
      it("if response OK dispatch clearSelectedDatasource, setSelectedDatasource", done => {
        store
          .dispatch(
            updateDatasourceColumns("form-table", "columns", "temperature")
          )
          .then(() => {
            actions = store.getActions();
            expect(store.getActions()).toHaveLength(1);
            expect(actions[0]).toHaveProperty("type", SET_DATASOURCE_COLUMNS);
            done();
          })
          .catch(done.fail);
      });

      it("if API response is not ok, dispatch ADD_ERRORS", done => {
        putDatasourceColumn.mockImplementation(mockApi({errors: true}));
        store
          .dispatch(
            updateDatasourceColumns("form-table", "columns", "temperature")
          )
          .then(() => {
            expect(store.getActions()).toHaveLength(2);
            expect(store.getActions()[0]).toHaveProperty("type", ADD_ERRORS);
            expect(store.getActions()[1]).toHaveProperty("type", ADD_TOAST);
            done();
          })
          .catch(done.fail);
      });
    });

    // fine describe
  });
});
