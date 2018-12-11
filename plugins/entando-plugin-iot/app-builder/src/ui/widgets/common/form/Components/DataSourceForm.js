import React from "react";
import {Field} from "redux-form";
import {required} from "@entando/utils";
import {InputGroup} from "patternfly-react";
import RenderSelectInput from "ui/common/form/RenderSelectInput";
import FormLabel from "ui/common/form/FormLabel";

const DataSourceForm = () => {
  const selectOptionsDataSource = [
    {
      value: "entando-iot-server",
      text: "Entando IoT Server"
    }
  ];
  return (
    <InputGroup className="DataSourceForm__input-group">
      <Field
        component={RenderSelectInput}
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

export default DataSourceForm;
