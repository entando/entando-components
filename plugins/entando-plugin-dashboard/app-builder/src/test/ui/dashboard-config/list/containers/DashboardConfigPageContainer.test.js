import {
  mapStateToProps,
  mapDispatchToProps,
} from 'ui/dashboard-config/list/containers/DashboardConfigPageContainer';

import { DASHBOARD_CONFIG_LIST } from 'mocks/dashboardConfigs';

import { fetchServerConfigList, checkStatusServerConfig, gotoPluginPage } from 'state/main/actions';
import { getServerConfigList } from 'state/main/selectors';

jest.mock('state/main/actions');
jest.mock('state/main/selectors');

fetchServerConfigList.mockImplementation(() => Promise.resolve());
checkStatusServerConfig.mockImplementation(() => ({ 1: { status: 'online' } }));

getServerConfigList.mockImplementation(() => DASHBOARD_CONFIG_LIST);


const dispatchMock = jest.fn(() => Promise.resolve({}));

const INITIAL_STATE = {
  dashboardConfig: {
    servers: [],
  },
};
describe('DashboardConfigPageContainer', () => {
  let props;
  describe('mapStateToProps', () => {
    beforeEach(() => {
      props = mapStateToProps(INITIAL_STATE);
    });
    it('maps servers property state in DashboardConfigPage', () => {
      expect(props).toHaveProperty('serverList');
      expect(props).toHaveProperty('serverCheck');
      expect(props.serverList).toEqual(expect.arrayContaining(DASHBOARD_CONFIG_LIST));
    });
  });
  describe('mapDispatchToProps', () => {
    beforeEach(() => {
      props = mapDispatchToProps(dispatchMock);
    });
    it('should map the correct function properties', () => {
      expect(props.onWillMount).toBeDefined();
      expect(props.removeConfigItem).toBeDefined();
      expect(props.editConfigItem).toBeDefined();
      expect(props.testConfigItem).toBeDefined();
      expect(props.testAllConfigItems).toBeDefined();
      expect(props.gotoPluginPage).toBeDefined();
    });

    it('should dispatch an action if onWillMount is called', () => {
      props.onWillMount();
      expect(dispatchMock).toHaveBeenCalled();
      expect(fetchServerConfigList).toHaveBeenCalled();
    });

    it('should dispatch an action if gotoPluginPage is called', () => {
      props.gotoPluginPage();
      expect(dispatchMock).toHaveBeenCalled();
      expect(gotoPluginPage).toHaveBeenCalled();
    });

    it('should dispatch an action if removeConfigItem is called', () => {
      props.removeConfigItem();
      expect(dispatchMock).toHaveBeenCalled();
    });

    it('should dispatch an action if editConfigItem is called', () => {
      props.editConfigItem();
      expect(dispatchMock).toHaveBeenCalled();
    });

    it('should dispatch an action if testConfigItem is called', () => {
      props.testConfigItem();
      expect(dispatchMock).toHaveBeenCalled();
    });

    it('should dispatch an action if testAllConfigItems is called', () => {
      props.testAllConfigItems();
      expect(dispatchMock).toHaveBeenCalled();
    });
  });
});
