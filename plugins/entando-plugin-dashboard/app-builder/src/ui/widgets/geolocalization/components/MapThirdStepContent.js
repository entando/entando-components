import React from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import { Grid, Row, Col, FormGroup, ControlLabel } from 'patternfly-react';

import FormattedMessage from 'ui/i18n/FormattedMessage';
import FormLabel from 'ui/common/form/FormLabel';

const renderField = ({
  input,
  label,
  min,
  max,
  direction,
  className,
  disabled,
  meta: { touched, error },
}) => (
  <FormGroup
    className="MapThirdStepContent__field-form-group"
    validationState={touched && error ? 'error' : null}
  >
    <ControlLabel htmlFor={input.name} className={className}>
      <FormattedMessage id={label} />
    </ControlLabel>

    <input
      {...input}
      type="number"
      className="MapThirdStepContent__input-number"
      min={min}
      max={max}
      dir={direction}
      disabled={disabled}
    />
    <span className="MapThirdStepContent__input-number-addon">PX</span>
    {touched && error && (
    <span className="MapThirdStepContent__error help-block">{error}</span>
      )}
  </FormGroup>
);

const MapThirdStepContent = () => (
  <div className="MapThirdStepContent">
    <Grid className="MapThirdStepContent__container">
      <Row>
        <Col xs={12} className="MapThirdStepContent__title-container">
          <label className="MapThirdStepContent__title" htmlFor="generalSettings">
            <FormattedMessage id="plugin.chart.generalSettings" />
          </label>
        </Col>
        <Col xs={6} className="MapThirdStepContent__map-size-container">
          <Col xs={12}>
            <label className="MapThirdStepContent__map-size" htmlFor="mapSize">
              <FormattedMessage id="plugin.geolocalization.mapSize" />
            </label>
          </Col>
          <Col xs={12}>
            <Field
              type="number"
              component={renderField}
              name="responsiveSize.height"
              label="common.height"
              min="0"
              max="9999"
              direction="rtl"
              className="MapThirdStepContent__field-height"
            />
            <Field
              type="number"
              component={renderField}
              name="responsiveSize.width"
              label="common.width"
              min="0"
              max="9999"
              direction="rtl"
              className="MapThirdStepContent__field-width"
            />
          </Col>
        </Col>
        <Col xs={6} className="MapThirdStepContent__padding-container">
          <label className="MapThirdStepContent__padding" htmlFor="padding">
            <FormattedMessage id="plugin.geolocalization.padding" />
          </label>
          <Col xs={12}>
            <Field
              type="number"
              component={renderField}
              name="responsiveSize.top"
              label="common.top"
              min="0"
              max="9999"
              direction="rtl"
              className="MapThirdStepContent__field-top"
            />
            <Field
              type="number"
              component={renderField}
              name="responsiveSize.right"
              label="common.right"
              min="0"
              max="9999"
              direction="rtl"
              className="MapThirdStepContent__field-right"
            />
          </Col>
          <Col xs={12}>
            <Field
              type="number"
              component={renderField}
              name="responsiveSize.bottom"
              label="common.bottom"
              min="0"
              max="9999"
              direction="rtl"
              className="MapThirdStepContent__field-bottom"
            />
            <Field
              type="number"
              component={renderField}
              name="responsiveSize.left"
              label="common.left"
              min="0"
              max="9999"
              direction="rtl"
              className="MapThirdStepContent__field-left"
            />
          </Col>
        </Col>
        <Col xs={12}>
          <hr />
        </Col>
        <Col xs={12}>
          <Col xs={12}>
            <label className="MapThirdStepContent__legend-position" htmlFor="common.legendPosition">
              <FormLabel
                labelId="common.legendPosition"
                helpId="common.legendPosition.help"
              />
            </label>
          </Col>
          <Col xs={12}>
            <Col
              xs={4}
              className="MapThirdStepContent__legend-position-container-radio"
            >
              <Col xs={12}>
                <label className="radio-inline" htmlFor="legend.position">
                  <Field
                    component="input"
                    type="radio"
                    name="legend.position"
                    value="bottomleft"
                  />
                  <FormattedMessage id="common.topLeft" />
                </label>
                <label className="radio-inline" htmlFor="legend.position">
                  <Field
                    component="input"
                    type="radio"
                    name="legend.position"
                    value="topright"
                  />
                  <FormattedMessage id="common.topRight" />
                </label>
              </Col>
              <Col xs={12}>
                <label className="radio-inline" htmlFor="legend.position">
                  <Field
                    component="input"
                    type="radio"
                    name="legend.position"
                    value="bottomright"
                  />
                  <FormattedMessage id="common.bottomRight" />
                </label>
                <label className="radio-inline" htmlFor="legend.position">
                  <Field
                    component="input"
                    type="radio"
                    name="legend.position"
                    value="bottoleft"
                  />
                  <FormattedMessage id="common.bottomLeft" />
                </label>
              </Col>
            </Col>
          </Col>
        </Col>
      </Row>
    </Grid>
  </div>
);

renderField.propTypes = {
  input: PropTypes.shape({}).isRequired,
  label: PropTypes.string.isRequired,
  min: PropTypes.string.isRequired,
  max: PropTypes.string.isRequired,
  direction: PropTypes.string.isRequired,
  disabled: PropTypes.bool,
  className: PropTypes.string.isRequired,
  meta: PropTypes.shape({}).isRequired,
};

renderField.defaultProps = {
  disabled: false,
};

export default MapThirdStepContent;
