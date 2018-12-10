import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field, reduxForm } from 'redux-form';
import { Row, Col, FormGroup, Alert } from 'patternfly-react';
import { Button } from 'react-bootstrap';
import { formattedText, required, isNumber } from '@entando/utils';
import { FormattedMessage } from 'react-intl';

import FormattedMessageLocal from 'ui/i18n/FormattedMessage';
import RenderTextInput from 'ui/common/form/RenderTextInput';
import SwitchRenderer from 'ui/common/form/SwitchRenderer';
import FormSectionTitle from 'ui/common/form/FormSectionTitle';

const MESSAGE_PREFIX = 'plugin.entando-plugin-jpiot';

export class ServerConfigFormBody extends Component {
  componentWillMount() {
    if (this.props.onWillMount) {
      this.props.onWillMount(this.props);
    }
  }

  componentWillUnmount() {
    if (this.props.onWillUnmount) {
      this.props.onWillUnmount(this.props);
    }
  }

  render() {
    const {
      handleSubmit, invalid, submitting, testConnection, onAlertDismiss,
      connectionOutcome, gotoListPage,
    } = this.props;

    let alert = null;
    if (connectionOutcome) {
      if (connectionOutcome === 'SUCCESS') {
        alert = (
          <Alert type="success" onDismiss={onAlertDismiss}>
            <FormattedMessageLocal id="ServerConfigForm.connectionSuccess" />
          </Alert>
        );
      } else {
        alert = (
          <Alert type="danger" onDismiss={onAlertDismiss}>
            <FormattedMessageLocal id="ServerConfigForm.connectionFailure" />
          </Alert>
        );
      }
    }

    return (
      <form onSubmit={handleSubmit} className="ServerConfigForm form-horizontal">
        <Row>
          <Col xs={12}>
            {alert}
          </Col>
        </Row>
        <Row>
          <Col xs={12}>
            <FormSectionTitle
              titleId={`${MESSAGE_PREFIX}.ServerConfigForm.generalSettings`}
            />
          </Col>
        </Row>
        <Row>
          <Col xs={12}>
            <FormGroup>
              <label htmlFor="active" className="col-xs-2 control-label">
                <FormattedMessageLocal id="ConfigPage.active" />
              </label>
              <Col xs={4}>
                <Field
                  component={SwitchRenderer}
                  name="active"
                />
              </Col>
              <label htmlFor="debug" className="col-xs-2 control-label">
                <FormattedMessageLocal id="ConfigPage.debug" />
              </label>
              <Col xs={4}>
                <Field
                  component={SwitchRenderer}
                  name="debug"
                />
              </Col>
            </FormGroup>
          </Col>
        </Row>
        <Row>
          <Col xs={12}>
            <FormSectionTitle
              titleId={`${MESSAGE_PREFIX}.ServerConfigForm.connection`}
            />
          </Col>
        </Row>
        <Row>
          <Col xs={12}>
            <fieldset>
              <Field
                component={RenderTextInput}
                name="name"
                label={<FormattedMessageLocal id="ServerConfigForm.name" />}
                placeholder={formattedText('ServerConfigForm.name')}
                validate={[required]}
              />
            </fieldset>
            <fieldset>
              <Field
                component={RenderTextInput}
                name="hostName"
                label={<FormattedMessageLocal id="ServerConfigForm.hostName" />}
                placeholder={formattedText('ServerConfigForm.hostName')}
                validate={[required]}
              />
            </fieldset>

            <fieldset>
              <Field
                component={RenderTextInput}
                name="schema"
                label={<FormattedMessageLocal id="ServerConfigForm.schema" />}
                placeholder={formattedText('ServerConfigForm.schema')}
                validate={[required]}
              />
            </fieldset>
            <fieldset>
              <Field
                component={RenderTextInput}
                name="port"
                type="number"
                label={<FormattedMessageLocal id="ServerConfigForm.port" />}
                placeholder={formattedText('ServerConfigForm.port')}
                validate={[required, isNumber]}
              />
            </fieldset>
            <fieldset>
              <Field
                component={RenderTextInput}
                name="webappName"
                label={<FormattedMessageLocal id="ServerConfigForm.webappName" />}
                placeholder={formattedText('ServerConfigForm.webappName')}
              />
            </fieldset>
            <fieldset>
              <Field
                component={RenderTextInput}
                name="username"
                label={<FormattedMessageLocal id="ServerConfigForm.userName" />}
                placeholder={formattedText('ServerConfigForm.userName')}
                validate={[required]}
              />
            </fieldset>
            <fieldset>
              <Field
                component={RenderTextInput}
                type="password"
                name="password"
                label={<FormattedMessageLocal id="ServerConfigForm.password" />}
                placeholder={formattedText('ServerConfigForm.password')}
                validate={[required]}
              />
            </fieldset>
            <fieldset>
              <Field
                component={RenderTextInput}
                name="timeout"
                type="number"
                label={<FormattedMessageLocal id="ServerConfigForm.connTimeout" />}
                placeholder={formattedText('ServerConfigForm.connTimeout')}
                validate={[required, isNumber]}
              />
            </fieldset>
          </Col>
        </Row>
        <Row>
          <Col xs={12}>
            <div className="btn-toolbar pull-right">
              <Button
                className="ServerConfigForm__test-btn"
                type="button"
                bsStyle="primary"
                onClick={testConnection}
                disabled={invalid || submitting}
              >
                <FormattedMessageLocal id="ServerConfigForm.testConnection" />
              </Button>
            </div>
          </Col>
        </Row>
        <br />
        <Row>
          <Col xs={12}>
            <Button
              className="ServerConfigForm__cancel-btn"
              type="button"
              bsStyle="warning"
              onClick={() => gotoListPage()}
            >
              <FormattedMessage id="app.cancel" />
            </Button>

            <div className="btn-toolbar pull-right">
              <Button
                className="ServerConfigForm__save-btn"
                type="submit"
                bsStyle="primary"
                disabled={invalid || submitting}
              >
                <FormattedMessage id="app.save" />
              </Button>
            </div>
          </Col>
        </Row>
      </form>
    );
  }
}

ServerConfigFormBody.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onSubmit: PropTypes.func.isRequired,
  testConnection: PropTypes.func.isRequired,
  invalid: PropTypes.bool,
  submitting: PropTypes.bool,
  onWillMount: PropTypes.func,
  onWillUnmount: PropTypes.func,
  onAlertDismiss: PropTypes.func,
  gotoListPage: PropTypes.func.isRequired,
  connectionOutcome: PropTypes.string,
};

ServerConfigFormBody.defaultProps = {
  invalid: false,
  submitting: false,
  onWillMount: null,
  onWillUnmount: null,
  onAlertDismiss: null,
  connectionOutcome: '',
};

const ServerConfigForm = reduxForm({
  form: 'jpiot_serverConfig',
})(ServerConfigFormBody);

export default ServerConfigForm;
