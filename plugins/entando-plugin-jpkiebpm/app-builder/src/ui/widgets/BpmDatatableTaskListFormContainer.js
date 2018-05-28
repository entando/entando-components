
import { connect } from 'react-redux';
import { getFormValues, reduxForm } from 'redux-form';

import { getProcessList, getOverrides } from 'state/widgetConfig/selectors';
import { getServerConfigList } from 'state/main/selectors';
import { fetchProcessList, fetchOverrides } from 'state/widgetConfig/actions';
import { fetchServerConfigList } from 'state/main/actions';


import BpmDatatableTaskListForm from './BpmDatatableTaskListForm';

export const mapStateToProps = (state) => {
  const formState = getFormValues('widgetConfigForm')(state) || {};
  return ({
    knowledgeSources: getServerConfigList(state),
    processList: getProcessList(state),
    overrides: getOverrides(state).map(item => ({ ...item, visible: true, fieldOverride: '' })),
    selectedKnowledgeSource: formState.knowledgeSourcePath,
    selectedProcessId: formState.processPath,
    formOverrides: formState.overrides,
  });
};

export const mapDispatchToProps = dispatch => ({
  onWillMount: () => {
    dispatch(fetchServerConfigList());
  },

  fetchProcessList: (serverConfigId) => {
    dispatch(fetchProcessList(serverConfigId));
  },

  fetchOverrides: (serverConfigId, processId) => {
    dispatch(fetchOverrides(serverConfigId, processId));
  },
});

const mergeProps = (stateProps, dispatchProps, ownProps) => ({
  ...stateProps,
  ...dispatchProps,
  ...ownProps,
  onSubmit: (data) => {
    const transformedData = { ...data };
    transformedData.overrides = transformedData.overrides.map((item, i) => ({
      position: i + 1,
      visible: item.visible,
      fieldOverride: item.fieldOverride,
    }));
    ownProps.onSubmit(transformedData);
  },
});

const widgetConfigForm = reduxForm({
  form: 'widgetConfigForm',
})(BpmDatatableTaskListForm);

export default connect(mapStateToProps, mapDispatchToProps, mergeProps)(widgetConfigForm);
