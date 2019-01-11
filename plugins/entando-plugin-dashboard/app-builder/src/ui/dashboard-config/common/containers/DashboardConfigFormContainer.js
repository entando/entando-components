import {connect} from "react-redux";
import {formValueSelector} from "redux-form";

import DashboardConfigForm from "ui/dashboard-config/common/components/DashboardConfigForm";

const selector = formValueSelector("dashboard-config-form");

const mapStateToProps = state => ({
  datasourceValue: {
    name: selector(state, "datasource"),
    uri: selector(state, "datasourceURI")
  },
  datasources: selector(state, "datasources") || []
});

const mapDispatchToProps = () => ({
  onSubmit: values => {
    console.log("values", values);
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
