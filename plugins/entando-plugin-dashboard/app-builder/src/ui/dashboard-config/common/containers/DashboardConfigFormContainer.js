import {connect} from "react-redux";
import {formValueSelector} from "redux-form";

import DashboardConfigForm from "ui/dashboard-config/common/components/DashboardConfigForm";

import {createServerConfig, updateServerConfig} from "state/main/actions";

const selector = formValueSelector("dashboard-config-form");

const mapStateToProps = state => ({
  datasourceValue: {
    name: selector(state, "datasource"),
    uri: selector(state, "datasourceURI")
  },
  datasources: selector(state, "datasources") || []
});

const mapDispatchToProps = (dispatch, ownProps) => ({
  onSubmit: values => {
    console.log("values", values);
    if (ownProps.mode === "add") {
      dispatch(createServerConfig(values));
    } else if (ownProps.mode === "edit") {
      dispatch(updateServerConfig(values));
    }
  },
  testConnection: () => {
    console.log("test connection");
  }
});

const DashboardConfigFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardConfigForm);

export default DashboardConfigFormContainer;
