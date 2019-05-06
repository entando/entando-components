import { connect } from 'react-redux';
import DashboardConfigPage from 'ui/dashboard-config/list/components/DashboardConfigPage';

import {
  fetchServerConfigList,
  editServerConfig,
  removeServerConfig,
  checkServerConfig,
  gotoPluginPage,
} from 'state/main/actions';

import { getServerConfigList, getServerCheck } from 'state/main/selectors';

export const mapStateToProps = state => ({
  serverList: getServerConfigList(state),
  serverCheck: getServerCheck(state),
});

export const mapDispatchToProps = dispatch => ({
  onWillMount: () => {
    dispatch(fetchServerConfigList()).then(() => {
      dispatch(checkServerConfig());
    });
  },

  removeConfigItem: (id) => {
    dispatch(removeServerConfig(id));
  },

  editConfigItem: (formName, configItem) => {
    dispatch(editServerConfig(formName, configItem));
  },

  testConfigItem: (serverId) => { dispatch(checkServerConfig(serverId)); },

  testAllConfigItems: () => dispatch(checkServerConfig()),

  gotoPluginPage: page => dispatch(gotoPluginPage(page)),
});

const DashboardConfigPageContainer = connect(
  mapStateToProps,
  mapDispatchToProps,
)(DashboardConfigPage);

export default DashboardConfigPageContainer;
