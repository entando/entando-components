import { connect } from 'react-redux';
import { formValueSelector, getFormSyncErrors, change } from 'redux-form';
import { pick, omit } from 'lodash';

import { fetchServerConfigList, gotoConfigurationPage, fetchDefaultConfiguration } from 'state/main/actions';


import DashboardMapForm from 'ui/widgets/geolocalization/components/DashboardMapForm';

const selector = formValueSelector('form-dashboard-map');

const mapStateToProps = state => ({
  datasource: selector(state, 'datasource'),
  datasources: selector(state, 'datasources'),
  formSyncErrors: getFormSyncErrors('form-dashboard-map')(state),
  initialValues: {
    icon: { marker: 'fa-map-marker' },
    responsiveSize: {
      width: 300,
      height: 500,
      top: 50,
      right: 50,
      bottom: 50,
      left: 50,
    },
    legend: {
      position: 'topright',
    },
  },

});

const mapDispatchToProps = (dispatch, ownProps) => ({
  onWillMount: () => {
    dispatch(fetchServerConfigList());
    dispatch(fetchDefaultConfiguration()).then((config) => {
      dispatch(change('form-dashboard-map', 'serverName', config.dashboardId));
    });
  },
  onCancel: () => dispatch(gotoConfigurationPage()),
  onSubmit: (data) => {
    const transformData = {
      ...pick(data, ['datasource', 'title', 'serverName']),
    };
    transformData.configMap = {
      ...omit(data, ['datasource', 'title', 'serverName']),
    };
    console.log('Submit data ', transformData);
    ownProps.onSubmit({ config: JSON.stringify(transformData) });
  },

});

const DashboardMapFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps,
)(DashboardMapForm);

export default DashboardMapFormContainer;
