import React from 'react';
import PropTypes from 'prop-types';
import FormattedMessage from 'ui/i18n/FormattedMessage';
import {FieldLevelHelp} from 'patternfly-react';
import {formattedText} from '@entando/utils';

const FormLabel = ({labelId, langLabelId, helpId, required}) => {
  const requiredIcon = required
    ? (<sup><i className="fa fa-asterisk required-icon FormLabel__required-icon"/></sup>)
    : null;

  const langLabel = langLabelId
    ? (<span className="label FormLabel__language-label">
      <FormattedMessage id={langLabelId}/>
    </span>)
    : null;

  const fieldHelp = helpId
    ? (<FieldLevelHelp content={formattedText(helpId)}/>)
    : null;

  return (<span className="FormLabel">
    {langLabel}
    <FormattedMessage id={labelId}/>&nbsp; {requiredIcon}
    {fieldHelp}
  </span>);
};

FormLabel.propTypes = {
  labelId: PropTypes.string.isRequired,
  langLabelId: PropTypes.string,
  helpId: PropTypes.string,
  required: PropTypes.bool
};

FormLabel.defaultProps = {
  langLabelId: '',
  helpId: '',
  required: false
};
export default FormLabel;