import React from 'react';
import PropTypes from 'prop-types';
import { Switch } from 'patternfly-react';

const SwitchRenderer = ({
  input,
  trueValue,
  falseValue,
  disabled,
  className,
}) => {
  const switchValue =
    input.value === 'true' || input.value === true || input.value === trueValue;

  let wrapperClass = 'wrapper';
  if (className) {
    wrapperClass = `${wrapperClass} ${className}`;
  }

  return (
    <Switch
      disabled={disabled}
      {...input}
      value={switchValue}
      onChange={(el, val) => input.onChange(val ? trueValue : falseValue)}
      wrapperClass={wrapperClass}
    />
  );
};

SwitchRenderer.propTypes = {
  /* eslint-disable react/forbid-prop-types */
  trueValue: PropTypes.any,
  falseValue: PropTypes.any,
  /* eslint-enable react/forbid-prop-types */
  input: PropTypes.shape({
    value: PropTypes.oneOfType([PropTypes.string, PropTypes.bool]).isRequired,
    onChange: PropTypes.func.isRequired,
  }).isRequired,
  disabled: PropTypes.bool,
  className: PropTypes.string,
};

SwitchRenderer.defaultProps = {
  trueValue: true,
  falseValue: false,
  disabled: false,
  className: '',
};

export default SwitchRenderer;
