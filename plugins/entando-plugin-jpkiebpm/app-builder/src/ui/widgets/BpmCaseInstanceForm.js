import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import { Row, Col, FormGroup, Button } from 'patternfly-react';
import { required } from '@entando/utils';
import FormattedMessage from '../i18n/FormattedMessage';


const options = [...Array(10).keys()].map(i => (
  <option key={i} value={i + 1}>{i + 1}</option>
));

class BpmCaseInstanceForm extends Component {
  componentWillMount() {
    if (this.props.onWillMount) {
      this.props.onWillMount(this.props);
    }
  }

  render() {
    const {
      handleSubmit, invalid, submitting, widgetId, knowledgeSources, fetchDeploymentUnits,
      deploymentUnits, selectedKnowledgeSource, selectedDeploymentUnit,
    } = this.props;

    const knowledgeSourcesOptions = knowledgeSources && knowledgeSources.map(config => (
      <option key={config.id} value={config.id}>{config.name}</option>
    ));
    knowledgeSourcesOptions.unshift(<option key="empty" value="" />);

    const deploymentUnitsOptions = deploymentUnits && deploymentUnits.map(item => (
      <option key={item.containerId} value={item.containerId}>{item.containerId}</option>
    ));
    deploymentUnitsOptions.unshift(<option key="empty" value="" />);


    return (
      <form
        className="BpmCaseInstanceForm"
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
              <label htmlFor="displayedInMenu" className="control-label">
                <FormattedMessage id="common.knowledgeSource" />
              </label>
              <div className="input-group">
                <Field
                  id="knowledgeSource"
                  component="select"
                  className="form-control"
                  name="knowledgeSourcePath"
                  validate={[required]}
                  onChange={ev => fetchDeploymentUnits(ev.currentTarget.value)}
                >
                  {knowledgeSourcesOptions}
                </Field>
              </div>
            </Col>
          </Row>
        </FormGroup>
        <FormGroup>
          <Row className={selectedKnowledgeSource && deploymentUnits.length ? '' : 'hide'}>
            <Col xs={10}>
              <label htmlFor="displayedInMenu" className="control-label">
                <FormattedMessage id="common.deploymentUnit" />
              </label>
              <div className="input-group">
                <Field
                  id="displayedInMenu"
                  component="select"
                  className="form-control"
                  name="processPath"
                  validate={[required]}
                >
                  {deploymentUnitsOptions}
                </Field>
              </div>
            </Col>
          </Row>
        </FormGroup>
        <FormGroup>
          <Row className={selectedKnowledgeSource && selectedDeploymentUnit ? '' : 'hide'}>
            <Col xs={10}>
              <label htmlFor="displayedInMenu" className="control-label">
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
              disabled={invalid || submitting}
            >
              <FormattedMessage id="common.save" />
            </Button>
          </Col>
        </Row>
      </form>
    );
  }
}


BpmCaseInstanceForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onWillMount: PropTypes.func.isRequired,
  fetchDeploymentUnits: PropTypes.func.isRequired,
  widgetId: PropTypes.string.isRequired,
  knowledgeSources: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
  })),
  deploymentUnits: PropTypes.arrayOf(PropTypes.shape({
    containerId: PropTypes.string.isRequired,
  })),
  invalid: PropTypes.bool.isRequired,
  submitting: PropTypes.bool.isRequired,
  selectedKnowledgeSource: PropTypes.string,
  selectedDeploymentUnit: PropTypes.string,

};

BpmCaseInstanceForm.defaultProps = {
  knowledgeSources: [],
  deploymentUnits: [],
  selectedKnowledgeSource: '',
  selectedDeploymentUnit: '',
};

export default BpmCaseInstanceForm;
