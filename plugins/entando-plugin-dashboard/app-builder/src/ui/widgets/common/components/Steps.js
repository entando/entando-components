import React, { Fragment } from 'react';
import PropTypes from 'prop-types';
import { Wizard } from 'patternfly-react';
import { formattedText } from '@entando/utils';

const Steps = ({ stepIndex }) => (
  <Fragment>
    <Wizard.Step
      key={0}
      stepIndex={0}
      step={1}
      label="1"
      title={formattedText('common.firstStep')}
      activeStep={stepIndex + 1}
      className="Steps__step"
    />
    <Wizard.Step
      key={1}
      stepIndex={1}
      step={2}
      label="2"
      title={formattedText('common.secondStep')}
      activeStep={stepIndex + 1}
      className="Steps__step"
    />
    <Wizard.Step
      key={2}
      stepIndex={2}
      step={3}
      label="3"
      title={formattedText('common.thirdStep')}
      activeStep={stepIndex + 1}
      className="Steps__step"
    />
  </Fragment>
);

Steps.propTypes = {
  stepIndex: PropTypes.number.isRequired,
};

export default Steps;
