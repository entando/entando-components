import React from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import { Row, Col, FormGroup, Button } from 'patternfly-react';
import FormattedMessage from '../i18n/FormattedMessage';

const options = [...Array(10).keys()].map(i => (
  <option value="ksource">{`ksource${i}${1}`}</option>
));

const BpmCaseInstanceSelectorForm = ({ handleSubmit }) => (
  <form
    className="BpmCaseInstanceSelectorForm"
    onSubmit={(ev) => { ev.preventDefault(); handleSubmit(); }}
  >
    <h5>
      <i className="fa fa-puzzle-piece" />
    &nbsp;
      <FormattedMessage id="BpmCaseInstanceSelectorForm.widgetName" />
    </h5>
    <FormGroup>
      <Row>
        <Col xs={10}>
          <label htmlFor="displayedInMenu" className="control-label">
            <FormattedMessage id="BpmCaseInstanceSelectorForm.channel" />
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

BpmCaseInstanceSelectorForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
};

export default BpmCaseInstanceSelectorForm;
