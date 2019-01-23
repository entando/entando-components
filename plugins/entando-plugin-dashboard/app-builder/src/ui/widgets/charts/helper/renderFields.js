import React from "react";
import {FormGroup, ControlLabel} from "patternfly-react";
import FormattedMessage from "ui/i18n/FormattedMessage";

export const inputTextField = ({
  input,
  meta: {touched, error},
  label,
  append,
  disabled
}) => (
  <FormGroup validationState={touched && error ? "error" : null}>
    <ControlLabel htmlFor={input.name}>
      <FormattedMessage id={label} />
    </ControlLabel>
    <input
      {...input}
      id={input.name}
      type="text"
      className="form-control"
      disabled={disabled}
    />
    {append && <span className="AppendedLabel">{append}</span>}
    {touched && error && <span className="help-block">{error}</span>}
  </FormGroup>
);
