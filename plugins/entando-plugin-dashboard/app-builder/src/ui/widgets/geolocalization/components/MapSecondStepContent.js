import React from "react";
import PropTypes from "prop-types";
import {FieldArray} from "redux-form";
import FieldArrayDatasourceLayer from "./FieldArrayDatasourceLayer";

const MapSecondStepContent = ({
  datasourceSelected,
  label,
  formName,
  optionColumns,
  optionColumnSelected,
  datasourcesValue,
  clearInputDatasourceData
}) => {
  return (
    <FieldArray
      component={FieldArrayDatasourceLayer}
      label={label}
      name="datasources"
      formName={formName}
      datasourceSelected={datasourceSelected}
      optionColumns={optionColumns}
      optionColumnSelected={optionColumnSelected}
      datasourcesValue={datasourcesValue}
      clearInputDatasourceData={clearInputDatasourceData}
    />
  );
};

MapSecondStepContent.propTypes = {
  formName: PropTypes.string,
  optionColumns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  optionColumnSelected: PropTypes.arrayOf(PropTypes.shape({})).isRequired
};
export default MapSecondStepContent;
