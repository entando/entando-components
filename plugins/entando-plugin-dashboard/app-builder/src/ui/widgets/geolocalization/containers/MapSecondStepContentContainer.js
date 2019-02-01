import {connect} from "react-redux";
import {
  formValueSelector,
  clearFields,
  arrayRemoveAll,
  arrayPush,
  arrayRemove
} from "redux-form";
import MapSecondStepContent from "../components/MapSecondStepContent";

import {clearDatasourceColumns} from "state/main/actions";

import {getDatasourceColumns} from "state/main/selectors";

const mapStateToProps = (state, ownProps) => {
  const {formName} = ownProps;
  const selector = formValueSelector(formName);
  const optionColumns = getDatasourceColumns(state);

  return {
    formName,
    datasourcesValue: selector(state, "datasources"),
    datasourceSelected: selector(state, "datasource"),
    optionColumns,
    optionColumnSelected: selector(state, "datasourceColumns") || [],
    label: selector(state, "label")
  };
};

const mapsDispatchToProps = (dispatch, ownProps) => {
  const {formName} = ownProps;
  return {
    clearInputDatasourceData: () => {
      dispatch(clearFields(formName, false, false, "label", "datasource"));
      dispatch(clearDatasourceColumns());
      dispatch(arrayRemoveAll(formName, "datasourceColumns"));
    },
    addColumnOptionSelected: (fieldName, value) => {
      dispatch(arrayPush(formName, fieldName, value));
    },
    removeColumnOptionSelected: (fieldName, index) => {
      dispatch(arrayRemove(formName, fieldName, index));
    }
  };
};

const MapSecondStepContentContainer = connect(
  mapStateToProps,
  mapsDispatchToProps
)(MapSecondStepContent);
export default MapSecondStepContentContainer;
