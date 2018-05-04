
import { connect } from 'react-redux';
import { getFormValues } from 'redux-form';

import { getDeploymentUnits } from 'state/widgetConfig/selectors';
import { getServerConfigList } from 'state/main/selectors';
import { fetchDeploymentUnits } from 'state/widgetConfig/actions';
import { fetchServerConfigList } from 'state/main/actions';


import BpmCaseInstanceForm from './BpmCaseInstanceForm';

export const mapStateToProps = (state) => {
  const formState = getFormValues('widgetConfigForm')(state) || {};
  return ({
    knowledgeSources: getServerConfigList(state),
    deploymentUnits: getDeploymentUnits(state),
    selectedKnowledgeSource: formState.knowledgeSourcePath,
    selectedDeploymentUnit: formState.processPath,
  });
};

export const mapDispatchToProps = dispatch => ({
  onWillMount: () => {
    dispatch(fetchServerConfigList());
  },

  fetchDeploymentUnits: (serverConfigId) => {
    dispatch(fetchDeploymentUnits(serverConfigId));
  },
});


export default connect(mapStateToProps, mapDispatchToProps)(BpmCaseInstanceForm);
