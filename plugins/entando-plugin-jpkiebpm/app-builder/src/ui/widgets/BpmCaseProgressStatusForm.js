import React from 'react';
import PropTypes from 'prop-types';
import { Field, reduxForm } from 'redux-form';

import { Row, Col, FormGroup, Button } from 'patternfly-react';
import FormattedMessage from '../i18n/FormattedMessage';

const channelOptions = [...Array(10).keys()].map(i => (
  <option key={i} value={i + 1}>{i + 1}</option>
));
const BpmCaseProgressStatusForm = ({
  handleSubmit, widgetId, invalid, submitting,
}) => (

  <form
    className="BpmCaseProgressStatusForm"
    onSubmit={(ev) => { ev.preventDefault(); handleSubmit(); }}
  >
    <h5>
      <i className="fa fa-puzzle-piece" />
          &nbsp;
      <FormattedMessage id={`widgetName.${widgetId}`} />
    </h5>
    <FormGroup>
      <Row>
        <Col xs={10}>
          <label htmlFor="channel" className="control-label">
            <FormattedMessage id="common.channel" />
          </label>
          <div className="input-group">
            <Field
              id="channel"
              component="select"
              className="form-control"
              name="channel"
            >
              {channelOptions}
            </Field>
          </div>
        </Col>
      </Row>
    </FormGroup>
    <FormGroup>
      <Row>
        <Col xs={10}>
          <label htmlFor="case" className="control-label">
            <FormattedMessage id="BpmCaseProgressStatusForm.selectProgressBar" />
          </label>
          <div className="form-check">
            <Field
              id="basic"
              component="input"
              type="radio"
              className="form-check-input"
              name="progressBarType"
              value="basic"
            />&nbsp;
            <label className="form-check-label" htmlFor="basic">
              <FormattedMessage id="BpmCaseProgressStatusForm.progressBarBasic" />
            </label>
          </div>
          <div className="form-check">
            <Field
              id="stacked"
              component="input"
              type="radio"
              className="form-check-input"
              name="progressBarType"
              value="stacked"
            />&nbsp;
            <label className="form-check-label" htmlFor="stacked">
              <FormattedMessage id="BpmCaseProgressStatusForm.progressBarStacked" />
            </label>
          </div>
        </Col>
      </Row>
    </FormGroup>
    <FormGroup>
      <Row>
        <Col xs={10}>
          <label htmlFor="case" className="control-label">
            <FormattedMessage id="BpmCaseProgressStatusForm.otherFeatures" />
          </label>
          <div className="form-check">
            <Field
              id="showMilestones"
              component="input"
              type="checkbox"
              className="form-check-input"
              name="showMilestones"
            />&nbsp;
            <label className="form-check-label" htmlFor="showMilestones">
              <FormattedMessage id="BpmCaseProgressStatusForm.showMilestones" />
            </label>
          </div>
          <div className="form-check">
            <Field
              id="showNumTasks"
              component="input"
              type="checkbox"
              className="form-check-input"
              name="showNumberOfTasks"
            />&nbsp;
            <label className="form-check-label" htmlFor="showNumTasks">
              <FormattedMessage id="BpmCaseProgressStatusForm.showNumTasks" />
            </label>
          </div>
        </Col>
      </Row>
    </FormGroup>
    <Row>
      <Col xs={12}>
        <Button
          type="submit"
          bsStyle="primary"
          className="pull-right"
          disabled={invalid || submitting}
        >
          <FormattedMessage id="common.save" />
        </Button>
      </Col>
    </Row>
  </form>
);


BpmCaseProgressStatusForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  widgetId: PropTypes.string.isRequired,
  invalid: PropTypes.bool.isRequired,
  submitting: PropTypes.bool.isRequired,
};

BpmCaseProgressStatusForm.defaultProps = {
};

export default reduxForm({
  form: 'widgetConfigForm',
  initialValues: { channel: 1 },
})(BpmCaseProgressStatusForm);
