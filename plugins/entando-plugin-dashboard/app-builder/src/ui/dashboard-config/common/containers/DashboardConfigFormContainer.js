import { connect } from 'react-redux';
import { formValueSelector } from 'redux-form';
import { omit } from 'lodash';

import DashboardConfigForm from 'ui/dashboard-config/common/components/DashboardConfigForm';

import {
  fetchServerType,
  createServerConfig,
  updateServerConfig,
  setInternalRoute,
} from 'state/main/actions';

import { getServerType } from 'state/main/selectors';

const selector = formValueSelector('dashboard-config-form');

export const mapStateToProps = state => ({
  datasourceValue: {
    datasource: selector(state, 'datasource'),
    datasourceURI: selector(state, 'datasourceURI'),
  },
  datasourceCode: selector(state, 'datasourceCode'),
  datasources: selector(state, 'datasources'),
  serverTypeList: getServerType(state),
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
});

const DashboardConfigFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps,
)(DashboardConfigForm);

export default DashboardConfigFormContainer;
