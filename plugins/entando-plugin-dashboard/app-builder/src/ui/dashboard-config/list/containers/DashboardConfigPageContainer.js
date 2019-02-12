import {connect} from "react-redux";
import DashboardConfigPage from "ui/dashboard-config/list/components/DashboardConfigPage";

import {fetchServerConfigList, gotoPluginPage} from "state/main/actions";

import {getServerConfigList} from "state/main/selectors";

const mapStateToProps = state => ({
  serverList: getServerConfigList(state)
});

const mapDispatchToProps = dispatch => ({
  onWillMount: () => dispatch(fetchServerConfigList()),
  removeConfigItem: id => {
    console.log("removeConfigItem ", id);
  },
  editConfigItem: configItem => {
    console.log("editConfigItem", configItem);
  },
  testConfigItem: () => {
    console.log("Test config");
  },
  testAllConfigItems: () => {},
  gotoPluginPage: page => dispatch(gotoPluginPage(page))
});

const DashboardConfigPageContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardConfigPage);

export default DashboardConfigPageContainer;
