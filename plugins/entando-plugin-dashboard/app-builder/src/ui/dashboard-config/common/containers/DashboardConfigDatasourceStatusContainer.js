import { connect } from 'react-redux';

import DashboardConfigDatasourceStatus from 'ui/dashboard-config/common/components/DashboardConfigDatasourceStatus';

const mapDispatchToProps = () => ({
  testConnection: () => {},
});

const DashboardConfigDatasourceStatusContainer = connect(
  null,
  mapDispatchToProps,
)(DashboardConfigDatasourceStatus);
export default DashboardConfigDatasourceStatusContainer;
