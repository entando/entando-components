import React from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import { required } from '@entando/utils';
import { InputGroup } from 'patternfly-react';
import RenderSelectInput from 'ui/common/form/RenderSelectInput';
import FormLabel from 'ui/common/form/FormLabel';

const DatasourceForm = ({
  datasources,
  onChange,
  labelSize,
  nameFieldArray,
  disabled,
}) => {
  const selectOptionsDatasource = datasources.map(m => ({
    value: m.datasourceCode || m.datasource,
    text: m.datasource,
  }));

  const nameField = nameFieldArray ?
    `${nameFieldArray}[datasource]` :
    'datasource';

  return (
    <InputGroup className="DatasourceForm__input-group">
      <Field
        component={RenderSelectInput}
        onChange={(ev, value) => onChange(value)}
        disabled={!!(disabled || datasources.length === 0)}
        options={selectOptionsDatasource}
        defaultOptionId="plugin.chooseAnOptionDatasource"
        label={
          <FormLabel
            labelId="plugin.datasource"
            helpId="plugin.datasource.help"
            required
          />
        }
        labelSize={labelSize}
        alignClass="text-left DashboardTableForm__no-padding-right"
        name={nameField}
        validate={[required]}
      />
    </InputGroup>
  );
};

DatasourceForm.propTypes = {
  datasources: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  onChange: PropTypes.func.isRequired,
  labelSize: PropTypes.number,
  nameFieldArray: PropTypes.string,
  disabled: PropTypes.bool,
};

DatasourceForm.defaultProps = {
  labelSize: 4,
  nameFieldArray: null,
  disabled: false,
};
export default DatasourceForm;
