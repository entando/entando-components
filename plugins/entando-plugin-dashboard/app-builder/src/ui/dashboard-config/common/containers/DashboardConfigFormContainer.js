import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import { omit } from 'lodash';


import DashboardConfigForm from 'ui/dashboard-config/common/components/DashboardConfigForm';

import {
  fetchServerType,
  createServerConfig,
  updateServerConfig,
  setInternalRoute,
  checkStatusDatasource,
  fetchPreviewDatasource,
} from 'state/main/actions';

import { getServerType, getDatasoucePreview } from 'state/main/selectors';

const selector = formValueSelector('dashboard-config-form');

export const mapStateToProps = state => ({
  datasourceValue: {
    datasource: selector(state, 'datasource'),
    datasourceURI: selector(state, 'datasourceURI'),
  },
  datasourceCode: selector(state, 'datasourceCode'),
  datasources: selector(state, 'datasources'),
  serverTypeList: getServerType(state),
  previewColumns: getDatasoucePreview(state),

});

export const mapDispatchToProps = (dispatch, ownProps) => ({
  onWillMount: () => dispatch(fetchServerType()),
  onSubmit: (values) => {
    const obj = omit(values, ['datasourceCode', 'datasource', 'datasourceURI']);
    if (ownProps.mode === 'add') {
      dispatch(createServerConfig(obj));
    } else if (ownProps.mode === 'edit') {
      dispatch(updateServerConfig(obj));
    }
  },
  gotoHomePage: () => {
    dispatch(setInternalRoute('home'));
  },
  testConnection: datasourceId => dispatch(checkStatusDatasource(datasourceId)),
  previewDatasource: (datasourceId) => {
    dispatch(fetchPreviewDatasource(datasourceId));
  },
});

const DashboardConfigFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps,
)(DashboardConfigForm);

export default DashboardConfigFormContainer;
