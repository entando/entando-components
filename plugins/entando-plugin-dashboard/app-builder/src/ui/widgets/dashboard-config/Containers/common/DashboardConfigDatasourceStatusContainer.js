import {connect} from "react-redux";

import DashboardConfigDatasourceStatus from "ui/widgets/dashboard-config/Components/common/DashboardConfigDatasourceStatus";

const mapDispatchToProps = () => ({
  testConnection: () => {
    console.log("test connection");
  }
});

const DashboardConfigDatasourceStatusContainer = connect(
  null,
  mapDispatchToProps
)(DashboardConfigDatasourceStatus);
export default DashboardConfigDatasourceStatusContainer;
