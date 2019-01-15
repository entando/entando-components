import React from "react";
import {FormGroup, ControlLabel} from "patternfly-react";
import FormattedMessage from "ui/i18n/FormattedMessage";
import {formattedText} from "@entando/utils";

export const inputTextField = ({
  input,
  meta: {touched, error},
  label,
  append,
  disabled
}) => (
  <FormGroup>
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

export const selectdField = ({
  input,
  meta: {touched, error},
  label,
  append,
  disabled,
  alignClass,
  help,
  defaultOptionId,
  options,
  optionReducer,
  optionValue,
  optionDisplayName
}) => {
  const defaultOption = defaultOptionId ? (
    <option value="">{formattedText(defaultOptionId)}</option>
  ) : null;

  const optionsList = optionReducer
    ? optionReducer(options)
    : options.map(item => (
        <option key={item[optionValue]} value={item[optionValue]}>
          {item[optionDisplayName]}
        </option>
      ));

  return (
    <FormGroup>
      {label ? (
        <ControlLabel htmlFor={input.name}>
          <FormattedMessage id={label} />
        </ControlLabel>
      ) : null}
      <select {...input} className="form-control" disabled={disabled}>
        {defaultOption}
        {optionsList}
      </select>
      {append && <span className="AppendedLabel">{append}</span>}
      {touched && error && <span className="help-block">{error}</span>}
    </FormGroup>
  );
};
