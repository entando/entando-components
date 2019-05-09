import React, { Component } from 'react';
import PropTypes from 'prop-types';
import {
  FormGroup,
  ControlLabel,
} from 'patternfly-react';
import FormattedMessage from 'ui/i18n/FormattedMessage';

class CustomFormat extends Component {
  constructor(props) {
    super(props);
    const { input: { onChange }, initValue } = props;
    if (initValue) {
      onChange(initValue);
    }
  //  this.handleChange = this.handleChange.bind(this);
  }

  // handleChange() {
  //
  // }
  render() {
    const { input, label, help } = this.props;
    return (
      <FormGroup>
        <ControlLabel htmlFor={input.name}>
          <input type="radio" name={input.name} id={input.name} />
          <input
            {...input}
            id={input.name}
            type="text"
            className="form-control"
          />
          <FormattedMessage id={label} />
          <br />
          <FormattedMessage id={help} />
        </ControlLabel>
      </FormGroup>);
  }
}

CustomFormat.propTypes = {
  input: PropTypes.shape({}).isRequired,
  label: PropTypes.string.isRequired,
  help: PropTypes.string.isRequired,
  initValue: PropTypes.string,
};

CustomFormat.defaultProps = {
  initValue: '',
};

export default CustomFormat;
