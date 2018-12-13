import React from "react";
import PropTypes from "prop-types";
import {Field} from "redux-form";
import {required} from "@entando/utils";
import {InputGroup} from "patternfly-react";
import RenderSelectInput from "ui/common/form/RenderSelectInput";
import FormLabel from "ui/common/form/FormLabel";

const ContextForm = ({contexts}) => {
  const selectOptionsContext = contexts.map(m => ({
    value: m.id,
    text: m.description
  }));

  return (
    <InputGroup className="ContextForm__input-group">
      <Field
        component={RenderSelectInput}
        disabled={contexts.length === 0 ? true : false}
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

ContextForm.propTypes = {
  contexts: PropTypes.arrayOf(PropTypes.shape({})).isRequired
};
export default ContextForm;
