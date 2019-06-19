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
  setDefaultConfiguration,
} from 'state/main/actions';

import { getServerType, getDatasoucePreview, getDatasourceCheck } from 'state/main/selectors';

const selector = formValueSelector('dashboard-config-form');

const optionDasourceTypeList = [
  {
    value: 'GENERIC',
    text: 'Generic',
  },
  {
    value: 'GATE',
    text: 'Gate',
  },
  {
    value: 'GEODATA',
    text: 'Geodata',
  },
];

export const mapStateToProps = state => ({
  datasourceValue: {
    datasource: selector(state, 'datasource'),
    datasourceURI: selector(state, 'datasourceURI'),
    datasourceType: selector(state, 'datasourceType'),
    defaultDatasource: selector(state, 'defaultDatasource'),
  },
  datasourceCode: selector(state, 'datasourceCode'),
  datasources: selector(state, 'datasources'),
  serverTypeList: getServerType(state),
  previewColumns: getDatasoucePreview(state),
  datasourceCheck: getDatasourceCheck(state),
  optionDasourceTypeList,
  serverId: selector(state, 'id'),

});

export const mapDispatchToProps = (dispatch, ownProps) => ({
  onWillMount: () => dispatch(fetchServerType()),
  onSubmit: (values) => {
    const obj = omit(values, ['datasource', 'datasourceURI', 'datasourceType']);
    if (ownProps.mode === 'add') {
      dispatch(createServerConfig(obj)).then((json) => {
        if (obj.defaultDatasource) {
          dispatch(setDefaultConfiguration(json.id, obj.datasourceCode));
        }
      });
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
  setDefaultDatasource: (serverId, datasourceCode) => {
    dispatch(setDefaultConfiguration(serverId, datasourceCode));
  },
});

const mergeProps = (stateProps, dispatchProps) => ({
  ...stateProps,
  ...dispatchProps,
  setDefaultDatasource: (datasourceCode) => {
    if (stateProps.serverId) {
      dispatchProps.setDefaultDatasource(stateProps.serverId, datasourceCode);
    }
  },
});

const DashboardConfigFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps,
  mergeProps,
)(DashboardConfigForm);

export default DashboardConfigFormContainer;
