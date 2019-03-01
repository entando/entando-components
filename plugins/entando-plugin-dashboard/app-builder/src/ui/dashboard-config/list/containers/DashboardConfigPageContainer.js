import {connect} from "react-redux";
import DashboardConfigPage from "ui/dashboard-config/list/components/DashboardConfigPage";

import {
  fetchServerConfigList,
  editServerConfig,
  removeServerConfig,
  gotoPluginPage
} from "state/main/actions";

import {getServerConfigList} from "state/main/selectors";

export const mapStateToProps = state => ({
  serverList: getServerConfigList(state)
});

export const mapDispatchToProps = dispatch => ({
  onWillMount: () => dispatch(fetchServerConfigList()),

  removeConfigItem: id => {
    dispatch(removeServerConfig(id));
  },

  editConfigItem: (formName, configItem) => {
    dispatch(editServerConfig(formName, configItem));
  },

  testConfigItem: () => {},

  testAllConfigItems: () => {},

  gotoPluginPage: page => dispatch(gotoPluginPage(page))
});

const DashboardConfigPageContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardConfigPage);

export default DashboardConfigPageContainer;
