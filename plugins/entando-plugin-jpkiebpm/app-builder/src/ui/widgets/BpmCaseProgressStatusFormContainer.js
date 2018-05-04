
import { connect } from 'react-redux';
import { getFormValues } from 'redux-form';

import { getDeploymentUnits, getCaseDefinitions } from 'state/widgetConfig/selectors';
import { getServerConfigList } from 'state/main/selectors';
import { fetchDeploymentUnits, fetchCaseDefinitions } from 'state/widgetConfig/actions';
import { fetchServerConfigList } from 'state/main/actions';


import BpmCaseProgressStatusForm from './BpmCaseProgressStatusForm';

export const mapStateToProps = (state) => {
  const formState = getFormValues('widgetConfigForm')(state) || {};
  return ({
    knowledgeSources: getServerConfigList(state),
    deploymentUnits: getDeploymentUnits(state),
    caseDefinitions: getCaseDefinitions(state),
    selectedKnowledgeSource: formState.knowledgeSourcePath,
    selectedDeploymentUnit: formState.processPath,
    selectedCaseDefinition: getCaseDefinitions(state).find(item => item.id === formState.casePath),
  });
};

export const mapDispatchToProps = dispatch => ({
  onWillMount: () => {
    dispatch(fetchServerConfigList());
  },

  fetchDeploymentUnits: (serverConfigId) => {
    dispatch(fetchDeploymentUnits(serverConfigId));
  },

  fetchCaseDefinitions: (knowledgeSourceId, deploymentUnitId) => {
    dispatch(fetchCaseDefinitions(knowledgeSourceId, deploymentUnitId));
  },
});


export default connect(mapStateToProps, mapDispatchToProps)(BpmCaseProgressStatusForm);
