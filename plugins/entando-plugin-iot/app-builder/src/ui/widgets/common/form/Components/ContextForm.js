import React from "react";
import {Field} from "redux-form";
import {required} from "@entando/utils";
import {InputGroup} from "patternfly-react";
import RenderSelectInput from "ui/common/form/RenderSelectInput";
import FormLabel from "ui/common/form/FormLabel";

const ContextForm = () => {
  const selectOptionsContext = [
    {
      value: "parking",
      text: "Parking"
    },
    {
      value: "bikeSharing",
      text: "Bike Sharking"
    }
  ];
  return (
    <InputGroup className="ContextForm__input-group">
      <Field
        component={RenderSelectInput}
        options={selectOptionsContext}
        defaultOptionId="plugin.chooseAnOptionContext"
        label={
          <FormLabel
            labelId="plugin.kindOfContext"
            helpId="plugin.kindOfContext.help"
            required
          />
        }
        labelSize={4}
        alignClass="text-left TableListDevicesForm__no-padding-right"
        name="kindContext"
        validate={[required]}
      />
    </InputGroup>
  );
};
export default ContextForm;
