import React from 'react';
import PropTypes from 'prop-types';

import { FormattedMessage } from 'react-intl';

import { name } from '../../../package.json';

const CustomFormattedMessage = props =>
  <FormattedMessage {...props} id={`plugin.${name}.${props.id}`} />;

CustomFormattedMessage.propTypes = {
  id: PropTypes.string.isRequired,
};

export default CustomFormattedMessage;
