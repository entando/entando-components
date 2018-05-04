import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field } from 'redux-form';
import { Row, Col, FormGroup, Button } from 'patternfly-react';
import { required } from '@entando/utils';
import FormattedMessage from '../i18n/FormattedMessage';


const channelOptions = [...Array(10).keys()].map(i => (
  <option key={i} value={i + 1}>{i + 1}</option>
));

class BpmCaseProgressStatusForm extends Component {
  componentWillMount() {
    if (this.props.onWillMount) {
      this.props.onWillMount(this.props);
    }
  }

  render() {
    const {
      handleSubmit, invalid, submitting, widgetId, knowledgeSources, fetchDeploymentUnits,
      deploymentUnits, selectedKnowledgeSource, selectedDeploymentUnit, caseDefinitions,
      fetchCaseDefinitions, selectedCaseDefinition,
    } = this.props;

    const knowledgeSourcesOptions = knowledgeSources && knowledgeSources.map(config => (
      <option key={config.id} value={config.id}>{config.name}</option>
    ));
    knowledgeSourcesOptions.unshift(<option key="empty" value="" />);

    const deploymentUnitsOptions = deploymentUnits && deploymentUnits.map(item => (
      <option key={item.containerId} value={item.containerId}>{item.containerId}</option>
    ));
    deploymentUnitsOptions.unshift(<option key="empty" value="" />);

    const caseDefinitionsOptions = caseDefinitions && caseDefinitions.map(item => (
      <option key={item.id} value={item.id}>{item.name}</option>
    ));
    caseDefinitionsOptions.unshift(<option key="empty" value="" />);


    return (
      <form
        className="BpmCaseProgressStatusForm"
        onSubmit={(ev) => { ev.preventDefault(); handleSubmit(); }}
      >
        <h5>
          <i className="fa fa-puzzle-piece" />
          &nbsp;
          <FormattedMessage id={`widgetName.${widgetId}`} />
        </h5>
        <Field
          component="input"
          type="hidden"
          name="frontEndMilestonesData.case-id-prefix"
          value={selectedCaseDefinition && selectedCaseDefinition['case-id-prefix']}
        />
        <Field
          component="input"
          type="hidden"
          name="frontEndMilestonesData.name"
          value={selectedCaseDefinition && selectedCaseDefinition.name}
        />
        <Field
          component="input"
          type="hidden"
          name="frontEndMilestonesData.stages"
          value={selectedCaseDefinition && selectedCaseDefinition.stages}
        />
        <FormGroup>
          <Row>
            <Col xs={10}>
              <label htmlFor="knowledgeSource" className="control-label">
                <FormattedMessage id="common.knowledgeSource" />
              </label>
              <div className="input-group">
                <Field
                  id="knowledgeSource"
                  component="select"
                  className="form-control"
                  name="frontEndMilestonesData.knowledge-source-id"
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
              <label htmlFor="deploymentUnit" className="control-label">
                <FormattedMessage id="common.deploymentUnit" />
              </label>
              <div className="input-group">
                <Field
                  id="deploymentUnit"
                  component="select"
                  className="form-control"
                  name="frontEndMilestonesData.container-id"
                  validate={[required]}
                  onChange={
                    ev => fetchCaseDefinitions(selectedKnowledgeSource, ev.currentTarget.value)
                  }
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
          <Row className={selectedKnowledgeSource && selectedDeploymentUnit && caseDefinitions.length ? '' : 'hide'}>
            <Col xs={10}>
              <label htmlFor="case" className="control-label">
                <FormattedMessage id="BpmCaseProgressStatusForm.cases" />
              </label>
              <div className="input-group">
                <Field
                  id="case"
                  component="select"
                  className="form-control"
                  name="casePath"
                >
                  {caseDefinitionsOptions}
                </Field>
              </div>
            </Col>
          </Row>
        </FormGroup>
        <FormGroup>
          <Row className={selectedCaseDefinition ? '' : 'hide'}>
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
                  name="ui.progress-bar-type"
                  value="basic"
                />
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
                  name="ui.progress-bar-type"
                  value="stacked"
                />
                <label className="form-check-label" htmlFor="stacked">
                  <FormattedMessage id="BpmCaseProgressStatusForm.progressBarStacked" />
                </label>
              </div>
            </Col>
          </Row>
        </FormGroup>
        <FormGroup>
          <Row className={selectedCaseDefinition ? '' : 'hide'}>
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
                />
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
                  name="showNumTasks"
                />
                <label className="form-check-label" htmlFor="showNumTasks">
                  <FormattedMessage id="BpmCaseProgressStatusForm.showNumTasks" />
                </label>
              </div>
            </Col>
          </Row>
        </FormGroup>
        <FormGroup>
          <Row className={selectedCaseDefinition ? '' : 'hide'}>
            <Col xs={10}>
              <table className="table">
                <thead>
                  <tr>
                    <th><FormattedMessage id="BpmCaseProgressStatusForm.visible" /></th>
                    <th><FormattedMessage id="BpmCaseProgressStatusForm.milestoneName" /></th>
                    <th><FormattedMessage id="BpmCaseProgressStatusForm.completed" /></th>
                  </tr>
                </thead>
                <tbody>
                  {selectedCaseDefinition &&
                  selectedCaseDefinition.frontEndMilestonesData.milestones.map(item => (
                    <tr>
                      <td>
                        <input type="checkbox" />
                      </td>
                      <td>
                        {item.name}
                      </td>
                      <td>
                        <input type="text" />
                      </td>
                    </tr>
                  ))}
                </tbody>
              </table>
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


BpmCaseProgressStatusForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onWillMount: PropTypes.func.isRequired,
  fetchDeploymentUnits: PropTypes.func.isRequired,
  fetchCaseDefinitions: PropTypes.func.isRequired,
  widgetId: PropTypes.string.isRequired,
  knowledgeSources: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
  })),
  deploymentUnits: PropTypes.arrayOf(PropTypes.shape({
    containerId: PropTypes.string.isRequired,
  })),
  caseDefinitions: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
  })),
  invalid: PropTypes.bool.isRequired,
  submitting: PropTypes.bool.isRequired,
  selectedKnowledgeSource: PropTypes.string,
  selectedDeploymentUnit: PropTypes.string,
  selectedCaseDefinition: PropTypes.shape({

  }),
};

BpmCaseProgressStatusForm.defaultProps = {
  knowledgeSources: [],
  deploymentUnits: [],
  caseDefinitions: [],
  selectedKnowledgeSource: '',
  selectedDeploymentUnit: '',
  selectedCaseDefinition: null,
};

export default BpmCaseProgressStatusForm;
