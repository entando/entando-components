import React from 'react';
import PropTypes from 'prop-types';
import { Col } from 'patternfly-react';
import FormattedMessage from 'ui/i18n/FormattedMessage';

const FormSectionTitle = ({ titleId }) => (
  <div className="FormSectionTitle">
    <Col xs={12}>
      <div className="FormSectionTitle__title">
        <h2>
          <FormattedMessage id={titleId} />
        </h2>
      </div>
    </Col>
    <Col xs={12}>
      <div className="FormSectionTitle__required-fields text-right">
        *<FormattedMessage id="plugin.fieldsRequired" />
      </div>
    </Col>
  </div>
);

FormSectionTitle.propTypes = {
  titleId: PropTypes.string.isRequired,
};

export default FormSectionTitle;
