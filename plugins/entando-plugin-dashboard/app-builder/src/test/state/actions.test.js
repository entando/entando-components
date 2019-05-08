import { isFSA } from 'flux-standard-action';

import {
  setInfoPage,
  setLanguages,
  setServerType,
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
  setInternalRoute,
} from 'state/main/actions';

import { PAGE_CONFIGURATION, LANGUAGES } from 'mocks/appBuilder';
import {
  SERVER_TYPE_LIST,
  SERVER_PIA,
  DASHBOARD_CONFIG_LIST,
  DASHBOARD_LIST_DATASOURCE,
  DATASOURCE_TEMPERATURE_DATA,
} from 'mocks/dashboardConfigs';
import {
  SET_INFO_PAGE,
  SET_LANGUAGES,
  SET_SERVER_TYPE,
  SET_SERVER_CONFIG_LIST,
  ADD_SERVER_CONFIG,
  UPDATE_SERVER_CONFIG,
  REMOVE_SERVER_CONFIG,
  SET_DATASOURCE_LIST,
  SET_DATASOURCE_COLUMNS,
  CLEAR_DATASOURCE_COLUMNS,
  SET_DATASOURCE_DATA,
  SET_SELECTED_DATASOURCE,
  CLEAR_SELECTED_DATASOURCE,
  SET_INTERNAL_ROUTE,
} from 'state/main/types';

describe('state/main/actions', () => {
  let action;
  describe('actions creator', () => {
    describe('setInfoPage', () => {
      beforeEach(() => {
        action = setInfoPage(PAGE_CONFIGURATION);
      });

      it('is FSA compliant', () => {
        expect(isFSA(action)).toBe(true);
      });

      it('actions is correct setup ', () => {
        expect(action).toHaveProperty('type', SET_INFO_PAGE);
        expect(action).toHaveProperty('payload.info', PAGE_CONFIGURATION);
      });
    });

    describe('setLanguages', () => {
      beforeEach(() => {
        action = setLanguages(LANGUAGES);
      });

      it('is FSA compliant', () => {
        expect(isFSA(action)).toBe(true);
      });

      it('actions is correct setup ', () => {
        expect(action).toHaveProperty('type', SET_LANGUAGES);
        expect(action).toHaveProperty('payload.languages', LANGUAGES);
      });
    });

    describe('setServerConfigList', () => {
      beforeEach(() => {
        action = setServerConfigList(DASHBOARD_CONFIG_LIST);
      });

      it('is FSA compliant', () => {
        expect(isFSA(action)).toBe(true);
      });

      it('actions is correct setup ', () => {
        expect(action).toHaveProperty('type', SET_SERVER_CONFIG_LIST);
        expect(action).toHaveProperty(
          'payload.serverList',
          DASHBOARD_CONFIG_LIST,
        );
      });
    });

    describe('setServerType', () => {
      beforeEach(() => {
        action = setServerType(SERVER_TYPE_LIST);
      });

      it('is FSA compliant', () => {
        expect(isFSA(action)).toBe(true);
      });

      it('actions is correct setup ', () => {
        expect(action).toHaveProperty('type', SET_SERVER_TYPE);
        expect(action).toHaveProperty(
          'payload.serverTypeList',
          SERVER_TYPE_LIST,
        );
      });
    });

    describe('addServerConfig', () => {
      beforeEach(() => {
        action = addServerConfig(SERVER_PIA);
      });

      it('is FSA compliant', () => {
        expect(isFSA(action)).toBe(true);
      });

      it('actions is correct setup ', () => {
        expect(action).toHaveProperty('type', ADD_SERVER_CONFIG);
        expect(action).toHaveProperty('payload.server', SERVER_PIA);
      });
    });

    describe('updateServerConfig', () => {
      beforeEach(() => {
        action = updateServerConfigAction(SERVER_PIA);
      });

      it('is FSA compliant', () => {
        expect(isFSA(action)).toBe(true);
      });

      it('actions is correct setup ', () => {
        expect(action).toHaveProperty('type', UPDATE_SERVER_CONFIG);
        expect(action).toHaveProperty('payload.server', SERVER_PIA);
      });
    });

    describe('removeServerConfigSync', () => {
      beforeEach(() => {
        action = removeServerConfigSync('1');
      });

      it('is FSA compliant', () => {
        expect(isFSA(action)).toBe(true);
      });

      it('actions is correct setup ', () => {
        expect(action).toHaveProperty('type', REMOVE_SERVER_CONFIG);
        expect(action).toHaveProperty('payload.configId', '1');
      });
    });

    describe('setDatasourceList', () => {
      beforeEach(() => {
        action = setDatasourceList(DASHBOARD_LIST_DATASOURCE);
      });

      it('is FSA compliant', () => {
        expect(isFSA(action)).toBe(true);
      });

      it('actions is correct setup ', () => {
        expect(action).toHaveProperty('type', SET_DATASOURCE_LIST);
        expect(action).toHaveProperty(
          'payload.datasourceList',
          DASHBOARD_LIST_DATASOURCE,
        );
      });
    });

    describe('setDatasourceColumns', () => {
      beforeEach(() => {
        action = setDatasourceColumns(DATASOURCE_TEMPERATURE_DATA.columns);
      });

      it('is FSA compliant', () => {
        expect(isFSA(action)).toBe(true);
      });

      it('actions is correct setup ', () => {
        expect(action).toHaveProperty('type', SET_DATASOURCE_COLUMNS);
        expect(action).toHaveProperty(
          'payload.columns',
          DATASOURCE_TEMPERATURE_DATA.columns,
        );
      });
    });

    describe('clearDatasourceColumns', () => {
      beforeEach(() => {
        action = clearDatasourceColumns();
      });

      it('is FSA compliant', () => {
        expect(isFSA(action)).toBe(true);
      });

      it('actions is correct setup ', () => {
        expect(action).toHaveProperty('type', CLEAR_DATASOURCE_COLUMNS);
      });
    });

    describe('setDatasourceData', () => {
      beforeEach(() => {
        action = setDatasourceData(DATASOURCE_TEMPERATURE_DATA.data);
      });

      it('is FSA compliant', () => {
        expect(isFSA(action)).toBe(true);
      });

      it('actions is correct setup ', () => {
        expect(action).toHaveProperty('type', SET_DATASOURCE_DATA);
        expect(action).toHaveProperty(
          'payload.data',
          DATASOURCE_TEMPERATURE_DATA.data,
        );
      });
    });

    describe('setSelectedDatasource', () => {
      beforeEach(() => {
        action = setSelectedDatasource(DATASOURCE_TEMPERATURE_DATA.id);
      });

      it('is FSA compliant', () => {
        expect(isFSA(action)).toBe(true);
      });

      it('actions is correct setup ', () => {
        expect(action).toHaveProperty('type', SET_SELECTED_DATASOURCE);
        expect(action).toHaveProperty(
          'payload.datasourceId',
          DATASOURCE_TEMPERATURE_DATA.id,
        );
      });
    });

    describe('clearSelectedDatasource', () => {
      beforeEach(() => {
        action = clearSelectedDatasource();
      });

      it('is FSA compliant', () => {
        expect(isFSA(action)).toBe(true);
      });

      it('actions is correct setup ', () => {
        expect(action).toHaveProperty('type', CLEAR_SELECTED_DATASOURCE);
      });
    });

    describe('setInternalRoute', () => {
      beforeEach(() => {
        action = setInternalRoute('home');
      });

      it('is FSA compliant', () => {
        expect(isFSA(action)).toBe(true);
      });

      it('actions is correct setup ', () => {
        expect(action).toHaveProperty('type', SET_INTERNAL_ROUTE);
        expect(action).toHaveProperty('payload.route', 'home');
      });
    });
  });
});
