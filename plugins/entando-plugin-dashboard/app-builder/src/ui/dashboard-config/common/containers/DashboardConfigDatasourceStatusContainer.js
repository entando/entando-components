import { connect } from 'react-redux';

import { getDatasourceCheck } from 'state/main/selectors';

import DashboardConfigDatasourceStatus from 'ui/dashboard-config/common/components/DashboardConfigDatasourceStatus';

const mapStateToProps = (state, ownProps) => {
  const datasourceCheck = getDatasourceCheck(state);
  const check = datasourceCheck[ownProps.datasourceCode];
  return {
    status: check ? check.status : null,
  };
};

const DashboardConfigDatasourceStatusContainer =
  connect(mapStateToProps, null)(DashboardConfigDatasourceStatus);
export default DashboardConfigDatasourceStatusContainer;
