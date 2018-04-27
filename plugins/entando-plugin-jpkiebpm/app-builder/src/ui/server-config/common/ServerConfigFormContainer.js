
import { connect } from 'react-redux';
import { initialize } from 'redux-form';

import {
  createServerConfig, updateServerConfig, gotoPluginPage, testFormServerConfig,
  cleanConnectionOutcomes,
} from 'state/main/actions';

import { getConnectionOutcomes } from 'state/main/selectors';

import ServerConfigForm from './ServerConfigForm';

const toFormData = formValues => ({
  ...formValues,
  port: parseInt(formValues.port, 10),
  timeout: parseInt(formValues.timeout, 10),
});


export const mapStateToProps = state => ({
  connectionOutcome: getConnectionOutcomes(state)['@@form'],
});


export const mapDispatchToProps = (dispatch, ownProps) => ({
  onSubmit: (formValues) => {
    const formData = toFormData(formValues);
    let action = {};
    if (ownProps.mode === 'add') {
      action = createServerConfig(formData);
    } else if (ownProps.mode === 'edit') {
      action = updateServerConfig(formData);
    }

    dispatch(action).then(() => dispatch(gotoPluginPage('list')));
  },

  onWillMount: () => {
    dispatch(cleanConnectionOutcomes());
    if (ownProps.mode === 'add') {
      dispatch(initialize('jpkiebpm_serverConfig', {
        active: true,
        debug: false,
      }));
    }
  },

  onWillUnmount: () => {
    dispatch(cleanConnectionOutcomes());
  },

  onAlertDismiss: () => {
    dispatch(cleanConnectionOutcomes());
  },

  testConnection: () => {
    dispatch(testFormServerConfig());
  },

  gotoListPage: () => dispatch(gotoPluginPage('list')),
});

const ServerConfigFormContainer = connect(mapStateToProps, mapDispatchToProps)(ServerConfigForm);

export default ServerConfigFormContainer;
