import { connect } from 'react-redux';
import { formValueSelector, getFormSyncErrors } from 'redux-form';
import { pick, omit } from 'lodash';

import { fetchServerConfigList } from 'state/main/actions';

import DashboardMapForm from 'ui/widgets/geolocalization/components/DashboardMapForm';

const selector = formValueSelector('form-dashboard-map');

const mapStateToProps = state => ({
  datasource: selector(state, 'datasource'),
  datasources: selector(state, 'datasources'),
  formSyncErrors: getFormSyncErrors('form-dashboard-map')(state),
  initialValues: {
    icon: { marker: 'fa-map-marker' },
  },
});

const mapDispatchToProps = (dispatch, ownProps) => ({
  onWillMount: () => {
    dispatch(fetchServerConfigList());
  },
  onSubmit: (data) => {
    const transformData = {
      ...pick(data, ['datasource', 'title', 'serverName']),
    };
    transformData.configMap = {
      ...omit(data, ['datasource', 'title', 'serverName']),
    };
    console.log('Submit data ', transformData);
    ownProps.onSubmit();
  },
});

const DashboardMapFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps,
)(DashboardMapForm);

export default DashboardMapFormContainer;
