import React from "react";
import PropTypes from "prop-types";
import {Field} from "redux-form";
import {required} from "@entando/utils";
import {InputGroup} from "patternfly-react";
import RenderSelectInput from "ui/common/form/RenderSelectInput";
import FormLabel from "ui/common/form/FormLabel";

const DatasourceForm = ({datasources}) => {
  const selectOptionsDatasource = datasources.map(m => ({
    value: m.id,
    text: m.datasource
  }));

  return (
    <InputGroup className="DatasourceForm__input-group">
      <Field
        component={RenderSelectInput}
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
        labelSize={4}
        alignClass="text-left DashboardTableForm__no-padding-right"
        name="datasource"
        validate={[required]}
      />
    </InputGroup>
  );
};

DatasourceForm.propTypes = {
  datasources: PropTypes.arrayOf(PropTypes.shape({})).isRequired
};
export default DatasourceForm;
