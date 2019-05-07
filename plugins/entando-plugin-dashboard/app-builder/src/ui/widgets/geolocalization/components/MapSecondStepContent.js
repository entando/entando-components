import React from 'react';
import PropTypes from 'prop-types';
import { FieldArray } from 'redux-form';
import FieldArrayDatasourceLayer from './FieldArrayDatasourceLayer';

const MapSecondStepContent = ({
  datasourceSelected,
  label,
  formName,
  optionColumns,
  optionColumnSelected,
  datasourcesValue,
  clearInputDatasourceData,
  addColumnOptionSelected,
  removeColumnOptionSelected,
}) => (
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
    addColumnOptionSelected={addColumnOptionSelected}
    removeColumnOptionSelected={removeColumnOptionSelected}
  />
);

MapSecondStepContent.propTypes = {
  formName: PropTypes.string,
  optionColumns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  optionColumnSelected: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  addColumnOptionSelected: PropTypes.func.isRequired,
  removeColumnOptionSelected: PropTypes.func.isRequired,
  datasourceSelected: PropTypes.string.isRequired,
  label: PropTypes.string.isRequired,
  datasourcesValue: PropTypes.string.isRequired,
  clearInputDatasourceData: PropTypes.func.isRequired,

};

MapSecondStepContent.defaultProps = {
  formName: '',
};
export default MapSecondStepContent;
