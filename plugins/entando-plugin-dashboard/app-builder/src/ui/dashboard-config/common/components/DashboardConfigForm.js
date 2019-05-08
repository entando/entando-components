import React, { Component } from 'react';
import { Field, FieldArray, reduxForm } from 'redux-form';
import PropTypes from 'prop-types';
import { FormGroup, Row, Col, Button } from 'patternfly-react';
import FormSectionTitle from 'ui/common/form/FormSectionTitle';
import {
  formattedText,
  required,
  minLength,
  maxLength,
  isNumber,
} from '@entando/utils';
import FormattedMessageLocal from 'ui/i18n/FormattedMessage';
import SwitchRenderer from 'ui/common/form/SwitchRenderer';
import RenderTextInput from 'ui/common/form/RenderTextInput';
import RenderSelectInput from 'ui/common/form/RenderSelectInput';
import FormLabel from 'ui/common/form/FormLabel';
import DashboardConfigDatasource from 'ui/dashboard-config/common/components/DashboardConfigDatasource';

const maxLength30 = maxLength(30);
const minLength3 = minLength(3);
const renderField = (
  name,
  labelId,
  placeholder,
  validate,
  labelSize,
  append,
) => {
  let requiredLabel = false;
  if (validate) {
    requiredLabel = validate.includes('required');
  }
  let pholder = '';
  if (placeholder) {
    pholder = placeholder;
  } else {
    pholder = labelId;
  }
  return (
    <Field
      component={RenderTextInput}
      name={name}
      label={<FormLabel labelId={labelId} required={requiredLabel} />}
      placeholder={formattedText(pholder)}
      validate={validate}
      labelSize={labelSize}
      alignClass="text-left"
      append={append}
    />
  );
};
export class DashboardConfigFormBody extends Component {
  componentWillMount() {
    this.props.onWillMount();
  }

  render() {
    const {
      handleSubmit,
      invalid,
      submitting,
      serverTypeList,
      datasources,
      datasourceValue,
      gotoHomePage,
      datasourceCode,
    } = this.props;

    const disableSubmit = invalid || submitting || datasources.length === 0;

    const optionServerTypeList = serverTypeList.map(m => ({
      value: m.code,
      text: m.description,
    }));

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


            </FormGroup>
            <div className="DashboardConfig__server-configure">
              <FormattedMessageLocal id="plugin.config.serverConfigure" />
            </div>
            <fieldset>
              <Row>
                <Col xs={12} sm={6}>
                  <Field
                    component={RenderSelectInput}
                    options={optionServerTypeList}
                    defaultOptionId="plugin.chooseAnOptionServerType"
                    label={<FormLabel labelId="plugin.serverType" />}
                    labelSize={2}
                    alignClass="text-left"
                    name="type"
                    validate={[required]}
                  />
                </Col>
              </Row>
              <Row>
                <Col xs={6}>
                  {renderField(
                    'serverDescription',
                    'plugin.config.serverDescription',
                    null,
                    [required, minLength3, maxLength30],
                    2,
                    formattedText('plugin.table.requirement'),
                  )}
                </Col>
                <Col xs={6}>
                  {renderField(
                    'serverURI',
                    'plugin.config.serverURI',
                    null,
                    [required],
                    2,
                  )}
                </Col>
              </Row>

              <Row>
                <Col xs={6}>
                  {renderField(
                    'username',
                    'plugin.config.username',
                    null,
                    [required],
                    2,
                  )}
                </Col>
                <Col xs={6}>
                  {renderField(
                    'password',
                    'plugin.config.password',
                    null,
                    [required],
                    2,
                  )}
                </Col>
              </Row>

              <Row>
                <Col xs={12}>
                  {renderField('token', 'plugin.config.token', null, null, 1)}
                </Col>
              </Row>

              <Row>
                <Col xs={6}>
                  {renderField(
                    'timeConnection',
                    'plugin.config.connectionTimeOut',
                    null,
                    [isNumber],
                    2,
                    formattedText('plugin.config.connectionTimeOutAppend'),
                  )}
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
                  'datasourceCode',
                  'plugin.config.datasourceCode',
                  null,
                  null,
                  2,
                  null,
                )}
              </Col>
            </Row>
            <Row>
              <Col xs={6}>
                {renderField(
                  'datasource',
                  'plugin.config.datasource',
                  null,
                  [minLength3, maxLength30],
                  2,
                  formattedText('plugin.table.requirement'),
                )}
              </Col>
              <Col xs={6}>
                {renderField(
                  'datasourceURI',
                  'plugin.config.datasourceURI',
                  null,
                  null,
                  2,
                  formattedText('plugin.config.datasourceURIExample'),
                )}
              </Col>
            </Row>
            <FieldArray
              name="datasources"
              component={DashboardConfigDatasource}
              datasourceValue={datasourceValue}
              datasources={datasources}
              datasourceCode={datasourceCode}

            />
            <Row>
              <Col xs={12}>
                <Button
                  className="DashboardConfig__btn-save pull-right"
                  type="submit"
                  bsStyle="primary"
                  disabled={disableSubmit}
                >
                  <FormattedMessageLocal id="common.save" />
                </Button>
                <Button
                  className="DashboardConfig__btn-cancel pull-right"
                  type="button"
                  bsStyle="default"
                  onClick={gotoHomePage}
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
  datasource: PropTypes.string,
  datasourceURI: PropTypes.string,
};
DashboardConfigFormBody.propTypes = {
  onWillMount: PropTypes.func.isRequired,
  handleSubmit: PropTypes.func.isRequired,
  gotoHomePage: PropTypes.func.isRequired,
  serverTypeList: PropTypes.arrayOf(PropTypes.shape({})).isRequired,
  datasources: PropTypes.arrayOf(PropTypes.shape(DATASOURCE_TYPE)),
  datasourceValue: PropTypes.shape(DATASOURCE_TYPE),
  datasourceCode: PropTypes.string,

  invalid: PropTypes.bool.isRequired,
  submitting: PropTypes.bool.isRequired,
};
DashboardConfigFormBody.defaultProps = {
  datasourceValue: {
    datasource: undefined,
    datasourceURI: undefined,
  },
  datasources: [],
  datasourceCode: '',
  datasourceCheck: {},
};
const DashboardConfigForm = reduxForm({
  form: 'dashboard-config-form',
})(DashboardConfigFormBody);
export default DashboardConfigForm;
