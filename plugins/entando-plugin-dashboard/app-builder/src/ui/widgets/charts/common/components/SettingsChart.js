import React, {Component} from "react";
import PropTypes from "prop-types";
import {Field, FieldArray} from "redux-form";
import {
  Row,
  Col,
  FormGroup,
  ControlLabel,
  FieldLevelHelp
} from "patternfly-react";

import {formattedText, required, minLength, maxLength} from "@entando/utils";
import FormattedMessage from "ui/i18n/FormattedMessage";
import SwitchRenderer from "ui/common/form/SwitchRenderer";

import FieldArrayDropDownMultiple from "ui/common/FieldArrayDropDownMultiple";

const maxLength30 = maxLength(30);
const minLength3 = minLength(3);

const inputTextField = ({
  input,
  meta: {touched, error},
  label,
  append,
  disabled
}) => (
  <FormGroup validationState={touched && error ? "error" : null}>
    <ControlLabel htmlFor={input.name}>
      <FormattedMessage id={label} />
    </ControlLabel>
    <input
      {...input}
      id={input.name}
      type="text"
      className="form-control"
      disabled={disabled}
    />
    {append && <span className="AppendedLabel">{append}</span>}
    {touched && error && <span className="help-block">{error}</span>}
  </FormGroup>
);

const wrapInputTextField = (name, label, append, disabled = false) => {
  const validate = [required, minLength3, maxLength30];
  if (disabled) {
    validate.shift();
  }
  return (
    <Field
      name={name}
      component={inputTextField}
      label={label}
      validate={validate}
      disabled={disabled}
      append={formattedText(append)}
    />
  );
};

class SettingsChart extends Component {
  chooseTimeFormat() {
    return (
      <div className="SettingsChart__timeformat-container">
        <div className="radio">
          <label>
            <Field
              name="axis.x.tick.format"
              component="input"
              type="radio"
              value="%Y-%m-%d"
            />
            <FormattedMessage id="plugin.chart.timeFormat-Y-M-D" />
          </label>
        </div>
        <div className="radio">
          <label>
            <Field
              name="axis.x.tick.format"
              component="input"
              type="radio"
              value="%d-%m-%Y"
            />
            <FormattedMessage id="plugin.chart.timeFormat-D-M-Y" />
          </label>
        </div>
        <div className="radio">
          <label>
            <Field
              name="axis.x.tick.format"
              component="input"
              type="radio"
              value="%m-%d-%Y"
            />
            <FormattedMessage id="plugin.chart.timeFormat-M-D-Y" />
          </label>
        </div>
        <div className="radio">
          <FormGroup>
            <ControlLabel>
              <FormattedMessage id="plugin.chart.timeFormatCustom" />
              &nbsp;
              <Field name="axis.x.tick.format" component="input" type="text" />
              <br />
              <FormattedMessage id="plugin.chart.timeFormatCustom.help" />
            </ControlLabel>
          </FormGroup>
        </div>
      </div>
    );
  }

  rederAxisX() {
    const {
      optionColumns,
      datasourceSelected,
      optionColumnXSelected,
      axisXType
    } = this.props;
    return (
      <Col xs={4} className="SettingsChart__col">
        <FormGroup className="SettingsChart__form-group">
          <ControlLabel>
            <FormattedMessage id="plugin.chart.Xaxis" />
          </ControlLabel>
        </FormGroup>
        {wrapInputTextField(
          "axis.x.label",
          "plugin.chart.labelXaxis",
          "plugin.table.requirement"
        )}
        <FieldArray
          className="SettingsChart__column-selected"
          name="columns.x"
          idKey={datasourceSelected}
          component={FieldArrayDropDownMultiple}
          optionColumns={optionColumns}
          optionColumnSelected={optionColumnXSelected}
          validate={[required]}
        />
        <FormGroup className="SettingsChart__form-group">
          <Field name="axis.x.type" component="select" className="form-control">
            <option value="indexed">Default</option>
            <option value="timeseries">Timeseries</option>
          </Field>
        </FormGroup>
        {axisXType === "timeseries" ? this.chooseTimeFormat() : null}
      </Col>
    );
  }

  rederAxisY() {
    const {
      optionColumns,
      datasourceSelected,
      optionColumnYSelected
    } = this.props;

    return (
      <Col xs={4} className="SettingsChart__col">
        <FormGroup className="SettingsChart__form-group">
          <ControlLabel>
            <FormattedMessage id="plugin.chart.Yaxis" />
          </ControlLabel>
        </FormGroup>
        {wrapInputTextField(
          "axis.y.label",
          "plugin.chart.labelYaxis",
          "plugin.table.requirement"
        )}
        <FieldArray
          className="SettingsChart__column-selected"
          name="columns.y"
          idKey={datasourceSelected}
          component={FieldArrayDropDownMultiple}
          optionColumns={optionColumns}
          optionColumnSelected={optionColumnYSelected}
          validate={[required]}
        />
      </Col>
    );
  }

  rederAxisY2() {
    const {
      optionColumns,
      datasourceSelected,
      optionColumnY2Selected
    } = this.props;
    return (
      <Col xs={4} className="SettingsChart__col">
        <FormGroup className="SettingsChart__form-group">
          <ControlLabel>
            <FormattedMessage id="plugin.chart.Y2axis" />
            <FieldLevelHelp
              content={<FormattedMessage id="plugin.chart.Y2axis.help" />}
              close="true"
            />
            <Field
              component={SwitchRenderer}
              name="axis.y2.show"
              className="pull-right"
            />
          </ControlLabel>
        </FormGroup>
        {wrapInputTextField(
          "axis.y2.label",
          "plugin.chart.labelY2axis",
          "plugin.table.requirement",
          !this.props.axisY2Show
        )}

        <FieldArray
          className="SettingsChart__column-selected"
          name="columns.y2"
          idKey={datasourceSelected}
          component={FieldArrayDropDownMultiple}
          optionColumns={optionColumns}
          optionColumnSelected={optionColumnY2Selected}
        />
      </Col>
    );
  }

  render() {
    return (
      <div className="SettingsChart">
        <Row>
          <Col xs={12}>
            <FormGroup className="SettingsChart__input-group">
              <div className="checkbox">
                <label>
                  <Field
                    component="input"
                    type="checkbox"
                    name="axis.rotated"
                  />
                  <strong>
                    <FormattedMessage id="plugin.chart.invertAxisXtoY" />
                  </strong>
                </label>
              </div>
            </FormGroup>
          </Col>
        </Row>
        <Row>
          {this.rederAxisX()}
          {this.rederAxisY()}
          {this.rederAxisY2()}
        </Row>
      </div>
    );
  }
}

SettingsChart.propTypes = {
  optionColumns: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  axisY2Show: PropTypes.bool.isRequired,
  axisXType: PropTypes.string.isRequired,
  selectedColumnsY: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  selectedColumnsY2: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  optionColumnYSelected: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  optionColumnY2Selected: PropTypes.arrayOf(PropTypes.shape({})).isRequired
};

export default SettingsChart;
