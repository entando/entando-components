import { connect } from 'react-redux';
import {
  formValueSelector,
  clearFields,
  arrayRemoveAll,
  arrayPush,
  arrayRemove,
} from 'redux-form';

import { clearDatasourceColumns } from 'state/main/actions';
import { getDatasourceColumns } from 'state/main/selectors';

import MapSecondStepContent from '../components/MapSecondStepContent';

const mapStateToProps = (state, ownProps) => {
  const { formName } = ownProps;
  const selector = formValueSelector(formName);
  const optionColumns = getDatasourceColumns(state);
  const datasourcesValue = selector(state, 'datasources') || [];
  const datasourceSelected = selector(state, 'datasource');
  const optionColumnSelected = selector(state, 'datasourceColumns') || [];
  const optionColumnsDeviceLocationsSelected = selector(state, 'datasourceColumnsDeviceLocations') || [];
  const label = selector(state, 'label');
  return {
    formName,
    datasourcesValue,
    datasourceSelected,
    optionColumns,
    optionColumnSelected,
    optionColumnsDeviceLocationsSelected,
    label,
  };
};

const mapsDispatchToProps = (dispatch, ownProps) => {
  const { formName } = ownProps;
  return {
    clearInputDatasourceData: () => {
      dispatch(clearFields(formName, false, false, 'label', 'datasource'));
      dispatch(clearDatasourceColumns());
      dispatch(arrayRemoveAll(formName, 'datasourceColumns'));
      dispatch(arrayRemoveAll(formName, 'datasourceColumnsDeviceLocations'));
    },
    addColumnOptionSelected: (fieldName, value) => {
      dispatch(arrayPush(formName, fieldName, value));
    },
    removeColumnOptionSelected: (fieldName, index) => {
      dispatch(arrayRemove(formName, fieldName, index));
    },
  };
};

const MapSecondStepContentContainer = connect(
  mapStateToProps,
  mapsDispatchToProps,
)(MapSecondStepContent);
export default MapSecondStepContentContainer;
