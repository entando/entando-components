import React, {Component} from "react";
import PropTypes from "prop-types";
import {Field} from "redux-form";
import {Row, Col, FormGroup, ControlLabel} from "patternfly-react";

import FormattedMessage from "ui/i18n/FormattedMessage";

const renderField = ({input, label, min, max, direction, className}) => {
  return (
    <FormGroup className="GeneralSettings__field-form-group">
      <ControlLabel htmlFor={input.name} className={className}>
        <FormattedMessage id={label} />
      </ControlLabel>

      <input
        {...input}
        type="number"
        className="GeneralSettings__input-number"
        min={min}
        max={max}
        dir={direction}
      />
      <div class="GeneralSettings__input-number-addon">PX</div>
    </FormGroup>
  );
};

class GeneralSettings extends Component {
  render() {
    return (
      <div className="GeneralSettings">
        <Row>
          <Col xs={12}>
            <label className="GeneralSettings__title">
              <FormattedMessage id="plugin.chart.generalSettings" />
            </label>
          </Col>
        </Row>
        <Row>
          <Col xs={4} className="GeneralSettings__col">
            <FormGroup className="GeneralSettings__form-group">
              <ControlLabel>
                <strong>
                  <FormattedMessage id="plugin.chart.chartSize" />
                </strong>
              </ControlLabel>
            </FormGroup>
          </Col>
          <Col xs={4} className="GeneralSettings__col">
            <FormGroup className="GeneralSettings__form-group">
              <ControlLabel>
                <strong>
                  <FormattedMessage id="plugin.chart.padding" />
                </strong>
              </ControlLabel>
            </FormGroup>
          </Col>
        </Row>
        <Row>
          <Col xs={4} className="GeneralSettings__col">
            <Field
              type="number"
              component={renderField}
              name="size.height"
              label="plugin.chart.height"
              min="0"
              max="9999"
              direction="rtl"
              className="GeneralSettings__field-height"
            />
          </Col>
          <Col xs={4} className="GeneralSettings__col">
            <Row>
              <Col xs={5}>
                <Field
                  type="number"
                  component={renderField}
                  name="size.top"
                  label="plugin.chart.top"
                  min="0"
                  max="9999"
                  direction="rtl"
                  className="GeneralSettings__field-top"
                />
              </Col>
              <Col xs={5}>
                <Field
                  type="number"
                  component={renderField}
                  name="size.right"
                  label="plugin.chart.right"
                  min="0"
                  max="9999"
                  direction="rtl"
                  className="GeneralSettings__field-right"
                />
              </Col>
            </Row>
          </Col>
        </Row>
        <Row>
          <Col xs={4} className="GeneralSettings__col">
            <Field
              type="number"
              component={renderField}
              name="size.with"
              label="plugin.chart.width"
              min="0"
              max="9999"
              direction="rtl"
              className="GeneralSettings__field-width"
            />
          </Col>
          <Col xs={4} className="GeneralSettings__col">
            <Row>
              <Col xs={5}>
                <Field
                  type="number"
                  component={renderField}
                  name="size.bottom"
                  label="plugin.chart.bottom"
                  min="0"
                  max="9999"
                  direction="rtl"
                  className="GeneralSettings__field-bottom"
                />
              </Col>
              <Col xs={5}>
                <Field
                  type="number"
                  component={renderField}
                  name="size.left"
                  label="plugin.chart.left"
                  min="0"
                  max="9999"
                  direction="rtl"
                  className="GeneralSettings__field-left"
                />
              </Col>
            </Row>
          </Col>
        </Row>
      </div>
    );
  }
}

export default GeneralSettings;
