import { connect } from 'react-redux';

import { checkStatusDatasource } from 'state/main/actions';

import { getDatasourceCheck } from 'state/main/selectors';

import DashboardConfigDatasourceStatus from 'ui/dashboard-config/common/components/DashboardConfigDatasourceStatus';

const mapStateToProps = (state, ownProps) => {
  const datasourceCheck = getDatasourceCheck(state);
  const check = datasourceCheck[ownProps.datasourceCode];
  return {
    datasourceCode: ownProps.datasourceCode,
    status: check ? check.status : null,
  };
};

const mapDispatchToProps = dispatch => ({
  testConnection: (datasourceId) => { dispatch(checkStatusDatasource(datasourceId)); },
});

const DashboardConfigDatasourceStatusContainer = connect(
  mapStateToProps,
  mapDispatchToProps,
)(DashboardConfigDatasourceStatus);
export default DashboardConfigDatasourceStatusContainer;
