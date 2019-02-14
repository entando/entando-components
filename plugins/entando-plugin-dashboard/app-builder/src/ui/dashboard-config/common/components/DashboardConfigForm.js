import React, {Component} from "react";
import {Field, FieldArray, reduxForm} from "redux-form";
import PropTypes from "prop-types";
import {FormGroup, Row, Col, Button} from "patternfly-react";
import FormSectionTitle from "ui/common/form/FormSectionTitle";
import {
  formattedText,
  required,
  minLength,
  maxLength,
  isNumber
} from "@entando/utils";

import FormattedMessageLocal from "ui/i18n/FormattedMessage";
import SwitchRenderer from "ui/common/form/SwitchRenderer";
import RenderTextInput from "ui/common/form/RenderTextInput";
import FormLabel from "ui/common/form/FormLabel";

import DashboardConfigDatasource from "ui/dashboard-config/common/components/DashboardConfigDatasource";

const maxLength30 = maxLength(30);
const minLength3 = minLength(3);

const renderField = (
  name,
  labelId,
  placeholder,
  validate,
  labelSize,
  append
) => {
  let required = false;
  if (validate) {
    required = validate.includes("required");
  }
  let pholder = "";
  if (placeholder) {
    pholder = placeholder;
  } else {
    pholder = labelId;
  }
  return (
    <Field
      component={RenderTextInput}
      name={name}
      label={<FormLabel labelId={labelId} required={required} />}
      placeholder={formattedText(pholder)}
      validate={validate}
      labelSize={labelSize}
      alignClass="text-left"
      append={append}
    />
  );
};

export class DashboardConfigFormBody extends Component {
  render() {
    const {handleSubmit} = this.props;
    return (
      <div className="DashboardConfig">
        <FormSectionTitle titleId="plugin.config.settings" />
        <form
          onSubmit={handleSubmit}
          className="DashboardConfig__form form-horizontal"
        >
          <legend className=" ">
            <FormGroup>
              <label htmlFor="active" className="col-xs-1 control-label">
                <FormattedMessageLocal id="common.active" />
              </label>
              <Col xs={1}>
                <Field component={SwitchRenderer} name="active" />
              </Col>
              <label htmlFor="debug" className="col-xs-1 control-label">
                <FormattedMessageLocal id="common.debug" />
              </label>
              <Col xs={1}>
                <Field component={SwitchRenderer} name="debug" disabled />
              </Col>
            </FormGroup>
            <div className="DashboardConfig__server-configure">
              <FormattedMessageLocal id="plugin.config.serverConfigure" />
            </div>
            <fieldset>
              <Row>
                <Col xs={6}>
                  {renderField(
                    "serverDescription",
                    "plugin.config.serverDescription",
                    null,
                    [required, minLength3, maxLength30],
                    2,
                    formattedText("plugin.table.requirement")
                  )}
                </Col>
                <Col xs={6}>
                  {renderField(
                    "serverURI",
                    "plugin.config.serverURI",
                    null,
                    [required],
                    2
                  )}
                </Col>
              </Row>

              <Row>
                <Col xs={6}>
                  {renderField(
                    "username",
                    "plugin.config.username",
                    null,
                    [required],
                    2
                  )}
                </Col>
                <Col xs={6}>
                  {renderField(
                    "password",
                    "plugin.config.password",
                    null,
                    [required],
                    2
                  )}
                </Col>
              </Row>

              <Row>
                <Col xs={12}>
                  {renderField("token", "plugin.config.token", null, null, 1)}
                </Col>
              </Row>

              <Row>
                <Col xs={6}>
                  {renderField(
                    "timeout",
                    "plugin.config.connectionTimeOut",
                    null,
                    [isNumber],
                    2,
                    formattedText("plugin.config.connectionTimeOutAppend")
                  )}
                </Col>
                <Col xs={6}>
                  <div className="btn-toolbar pull-right">
                    <Button
                      className="DashboardConfig__test-btn"
                      type="button"
                      bsStyle="default"
                      onClick={this.props.testConnection}
                    >
                      <FormattedMessageLocal id="plugin.config.testConnection" />
                    </Button>
                  </div>
                </Col>
              </Row>
            </fieldset>
          </legend>
          <div className="DashboardConfig__list-datasource">
            <FormattedMessageLocal id="plugin.config.listDatasource" />
          </div>
          <fieldset>
            <Row>
              <Col xs={6}>
                {renderField(
                  "datasource",
                  "plugin.config.datasource",
                  null,
                  [required, minLength3, maxLength30],
                  2,
                  formattedText("plugin.table.requirement")
                )}
              </Col>
              <Col xs={6}>
                {renderField(
                  "datasourceURI",
                  "plugin.config.datasourceURI",
                  null,
                  [required],
                  2,
                  formattedText("plugin.config.datasourceURIExample")
                )}
              </Col>
            </Row>
            <FieldArray
              name="datasources"
              component={DashboardConfigDatasource}
              datasourceValue={this.props.datasourceValue}
              datasources={this.props.datasources}
            />
            <Row>
              <Col xs={12}>
                <Button
                  className="DashboardConfig__btn-save pull-right"
                  type="submit"
                  bsStyle="primary"
                  disabled={this.props.invalid || this.props.submitting}
                >
                  <FormattedMessageLocal id="common.save" />
                </Button>
                <Button
                  className="DashboardConfig__btn-cancel pull-right"
                  type="button"
                  bsStyle="default"
                >
                  <FormattedMessageLocal id="common.cancel" />
                </Button>
              </Col>
            </Row>
          </fieldset>
        </form>
      </div>
    );
  }
}

const DATASOURCE_TYPE = {
  name: PropTypes.string,
  uri: PropTypes.string
};

DashboardConfigFormBody.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  testConnection: PropTypes.func.isRequired,
  datasources: PropTypes.arrayOf(PropTypes.shape(DATASOURCE_TYPE)),
  datasourceValue: PropTypes.shape(DATASOURCE_TYPE),
  invalid: PropTypes.bool.isRequired,
  submitting: PropTypes.bool.isRequired
};

DashboardConfigFormBody.defaultProps = {
  datasourceValue: {
    name: undefined,
    uri: undefined
  },
  datasources: []
};

const DashboardConfigForm = reduxForm({
  form: "dashboard-config-form"
})(DashboardConfigFormBody);

export default DashboardConfigForm;
