
import { connect } from 'react-redux';
import { getFormValues, reduxForm } from 'redux-form';
import { get } from 'lodash';

import { getDeploymentUnits, getCaseDefinitions } from 'state/widgetConfig/selectors';
import { getServerConfigList } from 'state/main/selectors';
import { fetchDeploymentUnits, fetchCaseDefinitions } from 'state/widgetConfig/actions';
import { fetchServerConfigList } from 'state/main/actions';


import BpmCaseProgressStatusForm from './BpmCaseProgressStatusForm';

export const mapStateToProps = (state) => {
  const formState = getFormValues('widgetConfigForm')(state) || {};
  const caseDef = getCaseDefinitions(state).find(item => item.id === formState.casePath);
  // add defaults to milestones
  if (caseDef && caseDef.milestones) {
    caseDef.milestones =
      caseDef.milestones.map(item => ({ visible: true, percentage: 20, ...item }));
  }

  return ({
    knowledgeSources: getServerConfigList(state),
    deploymentUnits: getDeploymentUnits(state),
    caseDefinitions: getCaseDefinitions(state),
    selectedKnowledgeSource: get(formState, 'frontEndMilestonesData.knowledge-source-id'),
    selectedDeploymentUnit: get(formState, 'frontEndMilestonesData.container-id'),
    formMilestones: get(formState, 'frontEndMilestonesData.milestones'),
    selectedCaseDefinition: caseDef,
    initialValues: { ui: { 'progress-bar-type': 'basic' } },
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

const mergeProps = (stateProps, dispatchProps, ownProps) => ({
  ...stateProps,
  ...dispatchProps,
  ...ownProps,
  onSubmit: (data) => {
    const caseDef = stateProps.selectedCaseDefinition;
    const transformedData = { ...data };
    transformedData.frontEndMilestonesData.name = caseDef.name;
    transformedData.frontEndMilestonesData.stages = caseDef.stages;
    transformedData.frontEndMilestonesData['case-id-prefix'] = caseDef['case-id-prefix'];
    transformedData.ui.additionalSettings = data.ui.additionalSettings ?
      Object.keys(data.ui.additionalSettings).filter(key => data.ui.additionalSettings[key]) :
      [];
    ownProps.onSubmit(transformedData);
  },
});

const widgetConfigForm = reduxForm({
  form: 'widgetConfigForm',
})(BpmCaseProgressStatusForm);

export default connect(mapStateToProps, mapDispatchToProps, mergeProps)(widgetConfigForm);
