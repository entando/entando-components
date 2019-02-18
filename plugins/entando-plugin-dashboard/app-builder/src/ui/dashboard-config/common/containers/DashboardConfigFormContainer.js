import {connect} from "react-redux";
import {formValueSelector} from "redux-form";

import DashboardConfigForm from "ui/dashboard-config/common/components/DashboardConfigForm";

import {createServerConfig, updateServerConfig} from "state/main/actions";

const selector = formValueSelector("dashboard-config-form");

export const mapStateToProps = state => ({
  datasourceValue: {
    datasource: selector(state, "datasource"),
    datasourceURI: selector(state, "datasourceURI")
  },
  datasources: selector(state, "datasources")
});

export const mapDispatchToProps = (dispatch, ownProps) => ({
  onSubmit: values => {
    if (ownProps.mode === "add") {
      dispatch(createServerConfig(values));
    } else if (ownProps.mode === "edit") {
      dispatch(updateServerConfig(values));
    }
  },
  testConnection: () => {}
});

const DashboardConfigFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardConfigForm);

export default DashboardConfigFormContainer;
