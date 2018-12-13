import {connect} from "react-redux";
import {initialize} from "redux-form";

import {
  createServerConfig,
  updateServerConfig,
  gotoPluginPage
} from "state/main/actions";

import ServerConfigForm from "./ServerConfigForm";

const toFormData = formValues => ({
  ...formValues,
  port: parseInt(formValues.port, 10),
  timeout: parseInt(formValues.timeout, 10)
});

export const mapDispatchToProps = (dispatch, ownProps) => ({
  onSubmit: formValues => {
    const formData = toFormData(formValues);
    let action = {};
    if (ownProps.mode === "add") {
      action = createServerConfig(formData);
    } else if (ownProps.mode === "edit") {
      action = updateServerConfig(formData);
    }

    dispatch(action).then(() => dispatch(gotoPluginPage("list")));
  },

  onWillMount: () => {
    if (ownProps.mode === "add") {
      dispatch(
        initialize("jpiot_serverConfig", {
          active: true,
          debug: false
        })
      );
    }
  },

  onWillUnmount: () => {},

  onAlertDismiss: () => {},

  gotoListPage: () => dispatch(gotoPluginPage("list"))
});

const ServerConfigFormContainer = connect(
  null,
  mapDispatchToProps
)(ServerConfigForm);

export default ServerConfigFormContainer;
