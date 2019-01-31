import React from "react";
import PropTypes from "prop-types";
import {Field} from "redux-form";
import {required} from "@entando/utils";
import {InputGroup} from "patternfly-react";
import RenderSelectInput from "ui/common/form/RenderSelectInput";
import RenderTextInput from "ui/common/form/RenderTextInput";
import FormLabel from "ui/common/form/FormLabel";

const FieldArrayDatasource = ({fields, datasources, onChange, className}) => {
  const selectOptionsDatasource = datasources.map(m => ({
    value: m.id,
    text: m.datasource
  }));
  return (
    <div className={`FieldArrayDatasource ${className}`}>
      <InputGroup className="FieldArrayDatasource__input-group">
        <Field
          component={RenderSelectInput}
          onChange={(ev, value) => onChange(value)}
          disabled={datasources.length === 0 ? true : false}
          options={selectOptionsDatasource}
          defaultOptionId="plugin.chooseAnOptionDatasource"
          label={
            <FormLabel
              labelId="plugin.datasource"
              helpId="plugin.datasource.help"
              required
            />
          }
          labelSize={2}
          alignClass="text-left"
          name="datasource"
          validate={[required]}
        />
      </InputGroup>
      <InputGroup className="FieldArrayDatasource__input-group">
        <Field
          component={RenderTextInput}
          label={
            <FormLabel
              labelId="plugin.datasource"
              helpId="plugin.datasource.help"
              required
            />
          }
          labelSize={2}
          alignClass="text-left"
          name="label"
          validate={[required]}
        />
      </InputGroup>
    </div>
  );
};

FieldArrayDatasource.propTypes = {
  fields: PropTypes.shape({}).isRequired,
  datasources: PropTypes.arrayOf(PropTypes.shape({})).isRequired
};

export default FieldArrayDatasource;
