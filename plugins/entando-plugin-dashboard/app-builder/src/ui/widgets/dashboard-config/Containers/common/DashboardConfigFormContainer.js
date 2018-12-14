import {connect} from "react-redux";
import DashboardConfigForm from "ui/widgets/dashboard-config/Components/common/DashboardConfigForm";

const mapDispatchToProps = () => ({
  onSubmit: values => {
    console.log("values", values);
  }
});

const DashboardConfigFormContainer = connect(
  null,
  mapDispatchToProps
)(DashboardConfigForm);

export default DashboardConfigFormContainer;
