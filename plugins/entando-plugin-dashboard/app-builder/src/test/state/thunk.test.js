import configureMockStore from "redux-mock-store";
import thunk from "redux-thunk";
import {initialize} from "redux-form";
import {ADD_ERRORS, ADD_TOAST} from "@entando/messages";

import {mockApi} from "../testUtils";

import {getTableWidgetConfig, fetchLanguages} from "state/main/actions";
import {getWidgetConfigSelector} from "state/app-builder/selectors";

import {getLanguages} from "api/appBuilder";

import {
  SET_DATASOURCE_LIST,
  SET_DATASOURCE_COLUMNS,
  SET_LANGUAGES
} from "state/main/types";
import {LANGUAGES} from "mocks/appBuilder";

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
  });
});
