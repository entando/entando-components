import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Field, FieldArray } from 'redux-form';
import { Row, Col, FormGroup, Button } from 'patternfly-react';
import { required } from '@entando/utils';
import FormattedMessage from '../i18n/FormattedMessage';
import OverridesTableRenderer from './OverridesTableRenderer';


class BpmFormByDatatypeForm extends Component {
  componentWillMount() {
    if (this.props.onWillMount) {
      this.props.onWillMount(this.props);
      if (this.props.selectedKnowledgeSource) {
        this.props.fetchProcessList(this.props.selectedKnowledgeSource);
        if (this.props.selectedProcessId) {
          this.props.fetchOverrides(
            this.props.selectedKnowledgeSource,
            this.props.selectedProcessId,
          );
        }
      }
    }
  }

  render() {
    const {
      handleSubmit, invalid, submitting, widgetId, knowledgeSources, fetchProcessList,
      processList, selectedKnowledgeSource, selectedProcessId, fetchOverrides,
      overrides, formOverrides,
    } = this.props;

    const knowledgeSourcesOptions = knowledgeSources && knowledgeSources.map(config => (
      <option key={config.id} value={config.id}>{config.name}</option>
    ));
    knowledgeSourcesOptions.unshift(<option key="empty" value="" />);

    const processListOptions = processList && processList.map(item => (
      <option key={item.processId} value={`${item.processId}@${item.containerId}`}>
        {item.processName} @ {item.containerId}
      </option>
    ));
    processListOptions.unshift(<option key="empty" value="" />);

    const bpmGroupsCheckboxes = ['admin', 'manager', 'appraiser', 'broker'].map(groupName => (
      <div key={`${groupName}Group`} className="form-check">
        <Field
          id={`${groupName}Group`}
          component="input"
          type="checkbox"
          className="form-check-input"
          name={`groups.${groupName}`}
        />&nbsp;
        <label className="form-check-label" htmlFor={`${groupName}Group`}>
          {groupName}
        </label>
      </div>
    ));

    return (
      <form
        className="BpmFormByDatatypeForm"
        onSubmit={(ev) => { ev.preventDefault(); handleSubmit(); }}
      >
        <h5>
          <i className="fa fa-puzzle-piece" />
          &nbsp;
          <FormattedMessage id={`widgetName.${widgetId}`} />
        </h5>
        <FormGroup className="form-horizontal">
          <Row>
            <Col xs={10}>
              <label htmlFor="knowledgeSource" className="control-label col-xs-2">
                <FormattedMessage id="common.knowledgeSource" />
              </label>
              <div className="input-group">
                <Field
                  id="knowledgeSource"
                  component="select"
                  className="form-control"
                  name="knowledgeSourcePath"
                  validate={[required]}
                  onChange={ev => fetchProcessList(ev.currentTarget.value)}
                >
                  {knowledgeSourcesOptions}
                </Field>
              </div>
            </Col>
          </Row>
        </FormGroup>
        <FormGroup className="form-horizontal">
          <Row className={selectedKnowledgeSource && processList.length ? '' : 'hide'}>
            <Col xs={10}>
              <label htmlFor="processPath" className="control-label col-xs-2">
                <FormattedMessage id="common.process" />
              </label>
              <div className="input-group">
                <Field
                  id="processPath"
                  component="select"
                  className="form-control"
                  name="processPath"
                  validate={[required]}
                  onChange={ev => fetchOverrides(selectedKnowledgeSource, ev.currentTarget.value)}
                >
                  {processListOptions}
                </Field>
              </div>
            </Col>
          </Row>
        </FormGroup>
        <FormGroup className="form-horizontal">
          <Row className={selectedKnowledgeSource && selectedProcessId ? '' : 'hide'}>
            <Col xs={10}>
              <label htmlFor="displayedInMenu" className="control-label col-xs-2">
                <FormattedMessage id="common.bpmGroups" />
              </label>
              <div className="input-group">
                { bpmGroupsCheckboxes }
              </div>
            </Col>
          </Row>
        </FormGroup>
        <FormGroup>
          <Row className={selectedProcessId ? '' : 'hide'}>
            <Col xs={10}>
              <FieldArray
                component={OverridesTableRenderer}
                name="overrides"
                initialValues={overrides}
                overrides={formOverrides}
              />
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


BpmFormByDatatypeForm.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onWillMount: PropTypes.func.isRequired,
  fetchProcessList: PropTypes.func.isRequired,
  fetchOverrides: PropTypes.func.isRequired,
  widgetId: PropTypes.string.isRequired,
  knowledgeSources: PropTypes.arrayOf(PropTypes.shape({
    id: PropTypes.string.isRequired,
    name: PropTypes.string.isRequired,
  })),
  processList: PropTypes.arrayOf(PropTypes.shape({
    containerId: PropTypes.string.isRequired,
    processName: PropTypes.string.isRequired,
    processId: PropTypes.string.isRequired,
  })),
  overrides: PropTypes.arrayOf(PropTypes.shape({

  })),
  formOverrides: PropTypes.arrayOf(PropTypes.shape({

  })),
  invalid: PropTypes.bool.isRequired,
  submitting: PropTypes.bool.isRequired,
  selectedKnowledgeSource: PropTypes.string,
  selectedProcessId: PropTypes.string,

};

BpmFormByDatatypeForm.defaultProps = {
  knowledgeSources: [],
  processList: [],
  overrides: null,
  formOverrides: [],
  selectedKnowledgeSource: '',
  selectedProcessId: '',
};

export default BpmFormByDatatypeForm;
