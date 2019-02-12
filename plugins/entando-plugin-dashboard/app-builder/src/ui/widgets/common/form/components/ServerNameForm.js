import React from "react";
import PropTypes from "prop-types";
import {Field} from "redux-form";
import {required} from "@entando/utils";
import {InputGroup} from "patternfly-react";
import RenderSelectInput from "ui/common/form/RenderSelectInput";
import FormLabel from "ui/common/form/FormLabel";

const ServerNameForm = ({serverNames, onChange, labelSize}) => {
  const selectOptionsServerName = serverNames.map(m => ({
    value: m.id,
    text: m.serverDescription
  }));
  return (
    <InputGroup className="ServerNameForm__input-group">
      <Field
        component={RenderSelectInput}
        onChange={onChange}
        options={selectOptionsServerName}
        defaultOptionId="plugin.chooseAnOptionServerName"
        label={
          <FormLabel
            labelId="plugin.serverName"
            helpId="plugin.serverName.help"
            required
          />
        }
        labelSize={labelSize}
        alignClass="text-left"
        name="serverName"
        validate={[required]}
      />
    </InputGroup>
  );
};

ServerNameForm.propTypes = {
  serverNames: PropTypes.arrayOf(
    PropTypes.shape({
      text: PropTypes.string,
      value: PropTypes.string
    })
  ).isRequired,
  onChange: PropTypes.func.isRequired,
  labelSize: PropTypes.number
};

ServerNameForm.defaultProps = {
  labelSize: 4
};

export default ServerNameForm;
