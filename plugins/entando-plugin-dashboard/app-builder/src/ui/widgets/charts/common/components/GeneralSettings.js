import React from "react";
import {Field} from "redux-form";
import {
  Grid,
  Row,
  Col,
  FormGroup,
  ControlLabel,
  FieldLevelHelp
} from "patternfly-react";
import FormattedMessage from "ui/i18n/FormattedMessage";
import SwitchRenderer from "ui/common/form/SwitchRenderer";

import {required} from "@entando/utils";

const renderField = ({
  input,
  label,
  min,
  max,
  direction,
  className,
  classInput,
  disabled,
  meta: {touched, error},
  addon = true
}) => {
  return (
    <FormGroup
      className="GeneralSettings__field-form-group"
      validationState={touched && error ? "error" : null}
    >
      <ControlLabel htmlFor={input.name} className={className}>
        <FormattedMessage id={label} />
      </ControlLabel>

      <input
        {...input}
        type="number"
        className={
          addon ? `GeneralSettings__input-number ${classInput}` : classInput
        }
        min={min}
        max={max}
        dir={direction}
        disabled={disabled}
      />
      {addon ? (
        <span className="GeneralSettings__input-number-addon">PX</span>
      ) : null}

      {touched && error && (
        <span className="GeneralSettings__error help-block">{error}</span>
      )}
    </FormGroup>
  );
};

const gaugeSettings = chart => (
  <Row>
    <Col xs={6}>
      <Row>
        <Col xs={12}>
          <label className="GeneralSettings__title">
            <FormattedMessage id="plugin.chart.gauge" />
          </label>
        </Col>
      </Row>
      <Row>
        <Col xs={12} className="GeneralSettings__col">
          <Row>
            <Col xs={6}>
              <Field
                type="number"
                component={renderField}
                name="gauge.min"
                label="plugin.chart.min"
                min={0}
                max={99}
                direction="rtl"
                classInput="GeneralSettings__input-field-gauge"
                addon={false}
              />
            </Col>
            <Col xs={6}>
              <Field
                type="number"
                component={renderField}
                name="gauge.max"
                label="plugin.chart.max"
                min={1}
                max={999999999}
                direction="rtl"
                classInput="GeneralSettings__input-field-gauge"
                validate={[required]}
                addon={false}
              />
            </Col>
          </Row>
        </Col>
      </Row>
    </Col>
  </Row>
);
const pieSettings = chart => (
  <Row>
    <Col xs={6}>
      <Row>
        <Col xs={12}>
          <label className="GeneralSettings__title">
            <FormattedMessage id="plugin.chart.pieSettings" />
          </label>
        </Col>
      </Row>
      <Row>
        <Col xs={12} className="GeneralSettings__col">
          <Row>
            <Col xs={6}>
              <FormGroup className="GeneralSettings__form-group">
                <ControlLabel>
                  <FormattedMessage id="plugin.chart.expand" />
                </ControlLabel>
                &nbsp;&nbsp;
                <Field component={SwitchRenderer} name="pie.expand" />
              </FormGroup>
            </Col>
          </Row>
        </Col>
      </Row>
    </Col>
  </Row>
);

const GeneralSettings = ({typeChart, chart}) => (
  <div className="GeneralSettings">
    <Grid
      fluid
      className={`GeneralSettings__general-settings-container ${typeChart}`}
    >
      <Row>
        <Col xs={12}>
          <label className="GeneralSettings__title">
            <FormattedMessage id="plugin.chart.generalSettings" />
          </label>
        </Col>
        <Col xs={4} className="GeneralSettings__col">
          <FormGroup className="GeneralSettings__form-group">
            <ControlLabel>
              <strong>
                <FormattedMessage id="plugin.chart.chartSize" />
              </strong>
            </ControlLabel>
          </FormGroup>
        </Col>
        <Col xs={8} className="GeneralSettings__col">
          <FormGroup className="GeneralSettings__form-group">
            <ControlLabel>
              <strong>
                <FormattedMessage id="plugin.chart.padding" />
              </strong>
            </ControlLabel>
          </FormGroup>
        </Col>
        <Col xs={4} className="GeneralSettings__col">
          <Field
            type="number"
            component={renderField}
            name="size.height"
            label="common.height"
            min={0}
            direction="rtl"
            className="GeneralSettings__field-height"
          />
        </Col>
        <Col xs={8} className="GeneralSettings__col">
          <Col xs={6}>
            <Field
              type="number"
              component={renderField}
              name="padding.top"
              label="common.top"
              min={0}
              direction="rtl"
              className="GeneralSettings__field-top"
            />
          </Col>
          <Col xs={6}>
            <Field
              type="number"
              component={renderField}
              name="padding.right"
              label="common.right"
              min={0}
              direction="rtl"
              className="GeneralSettings__field-right"
            />
          </Col>
        </Col>

        <Col xs={4} className="GeneralSettings__col">
          <Field
            type="number"
            component={renderField}
            name="size.width"
            label="common.width"
            min={0}
            direction="rtl"
            className="GeneralSettings__field-width"
          />
        </Col>
        <Col xs={8} className="GeneralSettings__col">
          <Col xs={6}>
            <Field
              type="number"
              component={renderField}
              name="padding.bottom"
              label="common.bottom"
              min={0}
              direction="rtl"
              className="GeneralSettings__field-bottom"
            />
          </Col>
          <Col xs={6}>
            <Field
              type="number"
              component={renderField}
              name="padding.left"
              label="common.left"
              min={0}
              direction="rtl"
              className="GeneralSettings__field-left"
            />
          </Col>
        </Col>

        <Col xs={12}>
          {typeChart === "LINE_CHART" || typeChart === "SPLINE_CHART" ? (
            <FormGroup className="GeneralSettings__form-group">
              <Field
                name="spline"
                id="spline"
                component="input"
                type="checkbox"
              />
              &nbsp;&nbsp;
              <ControlLabel>
                <strong>
                  <FormattedMessage id="plugin.chart.spline" />
                </strong>
              </ControlLabel>
            </FormGroup>
          ) : (
            <Field
              type="number"
              component={renderField}
              name={`${chart}.width`}
              label="plugin.chart.thickness"
              min={0}
              max={999}
              direction="rtl"
              className="GeneralSettings__field-thickness"
              disabled={typeChart === "LINE_CHART" ? true : false}
            />
          )}
        </Col>
      </Row>
    </Grid>
    <Grid
      fluid
      className={`GeneralSettings__interactive-chart-container ${typeChart}`}
    >
      {chart === "gauge" ? gaugeSettings(chart) : null}
      {chart === "pie" ? pieSettings(chart) : null}
      <Row>
        <Col xs={12} className="GeneralSettings__col">
          <FormGroup className="GeneralSettings__form-group">
            <ControlLabel>
              <strong>
                <FormattedMessage id="plugin.chart.interactiveChart" />
              </strong>
              <FieldLevelHelp
                content={<FormattedMessage id="plugin.chart.Y2axis.help" />}
                close="true"
              />
            </ControlLabel>
          </FormGroup>
        </Col>
      </Row>
      <Row>
        <Col xs={4} className="GeneralSettings__col">
          <FormGroup className="GeneralSettings__form-group">
            <ControlLabel>
              <FormattedMessage id="plugin.chart.showDetailMouseClick" />
            </ControlLabel>
            &nbsp;&nbsp;
            <Field component={SwitchRenderer} name="iteraction.enabled" />
          </FormGroup>
        </Col>
        <Col xs={2} className="GeneralSettings__col">
          <FormGroup className="GeneralSettings__form-group">
            <ControlLabel>
              <FormattedMessage id="common.legendPosition" />
            </ControlLabel>
          </FormGroup>
        </Col>
        <Col xs={4} className="GeneralSettings__col">
          <FormGroup className="GeneralSettings__form-group">
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
      </Row>
    </Grid>
  </div>
);

export default GeneralSettings;
