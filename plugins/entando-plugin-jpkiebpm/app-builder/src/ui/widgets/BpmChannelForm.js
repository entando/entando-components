import React from 'react';
import PropTypes from 'prop-types';
import { Field, reduxForm } from 'redux-form';
import { Row, Col, FormGroup, Button } from 'patternfly-react';
import FormattedMessage from '../i18n/FormattedMessage';


const options = [...Array(10).keys()].map(i => (
  <option key={i} value={i + 1}>{i + 1}</option>
));

const BpmChannelForm = ({ handleSubmit, widgetId }) => (
  <form
    className="BpmChannelForm"
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
              {options}
            </Field>
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
        >
          <FormattedMessage id="common.save" />
        </Button>
      </Col>
    </Row>
  </form>
);


BpmChannelForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  widgetId: PropTypes.string.isRequired,
};

export default reduxForm({
  form: 'widgetConfigForm',
})(BpmChannelForm);
