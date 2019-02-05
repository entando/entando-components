import React from "react";

import {Field} from "redux-form";
import {Grid, Row, Col, FormGroup, ControlLabel} from "patternfly-react";

import FormattedMessage from "ui/i18n/FormattedMessage";
import SwitchRenderer from "ui/common/form/SwitchRenderer";

const renderField = ({
  input,
  label,
  min,
  max,
  direction,
  className,
  disabled,
  meta: {touched, error}
}) => {
  return (
    <FormGroup
      className="MapThirdStepContent__field-form-group"
      validationState={touched && error ? "error" : null}
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
};

const MapThirdStepContent = () => {
  return (
    <div className="MapThirdStepContent">
      <Grid className="MapThirdStepContent__container">
        <Row>
          <Col xs={12} className="MapThirdStepContent__title-container">
            <label className="MapThirdStepContent__title">
              <FormattedMessage id="plugin.chart.generalSettings" />
            </label>
          </Col>
          <Col xs={6} className="MapThirdStepContent__map-size-container">
            <Col xs={12}>
              <label className="MapThirdStepContent__map-size">
                <FormattedMessage id="plugin.geolocalization.mapSize" />
              </label>
            </Col>
            <Col xs={12}>
              <FormGroup className="MapThirdStepContent__form-group">
                <Field component={SwitchRenderer} name="mapSize" />
                &nbsp;&nbsp;
                <ControlLabel>
                  <FormattedMessage id="plugin.geolocalization.mapSizeResponsive" />
                </ControlLabel>
              </FormGroup>
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
            <label className="MapThirdStepContent__padding">
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
            <Col
              xs={6}
              className="MapThirdStepContent__map-interactiveMap-container"
            >
              <Col xs={12}>
                <label className="MapThirdStepContent__interactive-map">
                  <FormattedMessage id="plugin.geolocalization.interactiveMap" />
                </label>
              </Col>
              <Col xs={12}>
                <FormGroup>
                  <ControlLabel>
                    <FormattedMessage id="plugin.geolocalization.interactiveMapDetails" />
                  </ControlLabel>
                  <br />
                  <Field component={SwitchRenderer} name="interactiveMap" />
                </FormGroup>
              </Col>
            </Col>
            <Col
              xs={6}
              className="MapThirdStepContent__legend-position-container"
            >
              <Col xs={12}>
                <label className="MapThirdStepContent__legend-position">
                  <FormattedMessage id="common.legendPosition" />
                </label>
              </Col>
              <Col xs={12}>
                <FormGroup className="MapThirdStepContent__form-group">
                  <label className="radio-inline">
                    <Field
                      component="input"
                      type="radio"
                      name="legend.position"
                      value="bottom"
                    />
                    <FormattedMessage id="common.bottom" />
                  </label>
                  <label className="radio-inline">
                    <Field
                      component="input"
                      type="radio"
                      name="legend.position"
                      value="right"
                    />
                    <FormattedMessage id="common.right" />
                  </label>
                </FormGroup>
              </Col>
            </Col>
          </Col>
        </Row>
      </Grid>
    </div>
  );
};

export default MapThirdStepContent;
