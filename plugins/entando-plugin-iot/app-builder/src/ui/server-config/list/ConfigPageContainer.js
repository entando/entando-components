import {connect} from "react-redux";
import {initialize} from "redux-form";

import {getServerConfigList, getConnectionOutcomes} from "state/main/selectors";
import {
  fetchServerConfigList,
  removeServerConfig,
  gotoPluginPage
} from "state/main/actions";

import ConfigPage from "./ConfigPage";

export const mapStateToProps = state => ({
  configList: getServerConfigList(state),
  connectionOutcomes: getConnectionOutcomes(state) || {}
});

export const mapDispatchToProps = dispatch => ({
  onWillMount: () => dispatch(fetchServerConfigList()),
  removeConfigItem: id => dispatch(removeServerConfig(id)),
  editConfigItem: configItem => {
    dispatch(gotoPluginPage("edit"));
    dispatch(initialize("form-jpiot-serverConfig", configItem));
  },
  gotoPluginPage: page => dispatch(gotoPluginPage(page))
});

const ConfigPageContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(ConfigPage);

export default ConfigPageContainer;
