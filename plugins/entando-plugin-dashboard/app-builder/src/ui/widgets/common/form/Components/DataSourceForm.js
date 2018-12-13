import React from "react";
import PropTypes from "prop-types";
import {Field} from "redux-form";
import {required} from "@entando/utils";
import {InputGroup} from "patternfly-react";
import RenderSelectInput from "ui/common/form/RenderSelectInput";
import FormLabel from "ui/common/form/FormLabel";

const DataSourceForm = ({datasources, onChange}) => {
  const selectOptionsDataSource = datasources.map(m => ({
    value: m.id,
    text: m.name
  }));
  return (
    <InputGroup className="DataSourceForm__input-group">
      <Field
        component={RenderSelectInput}
        onChange={onChange}
        options={selectOptionsDataSource}
        defaultOptionId="plugin.chooseAnOptionDataSource"
        label={
          <FormLabel
            labelId="plugin.datasource"
            helpId="plugin.datasource.help"
            required
          />
        }
        labelSize={4}
        alignClass="text-left"
        name="datasource"
        validate={[required]}
      />
    </InputGroup>
  );
};

DataSourceForm.propTypes = {
  datasources: PropTypes.arrayOf(
    PropTypes.shape({
      text: PropTypes.string,
      value: PropTypes.string
    })
  ).isRequired,
  onChange: PropTypes.func.isRequired
};

export default DataSourceForm;
