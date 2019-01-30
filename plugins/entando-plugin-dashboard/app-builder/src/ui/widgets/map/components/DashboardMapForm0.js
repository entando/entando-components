import React, {Component} from "react";
import PropTypes from "prop-types";
import {Button, Icon, Wizard} from "patternfly-react";

import Steps from "ui/widgets/common/components/Steps";
import ChartFirstStepContent from "ui/widgets/charts/common/components/ChartFirstStepContent";

const FORM_NAME = "form-dashboard-map";

class DashboardMapFormBody extends Component {
  constructor(props) {
    super(props);
    this.state = {
      activeSubStepIndex: 0
    };
    this.onNextButtonClick = this.onNextButtonClick.bind(this);
    this.onBackButtonClick = this.onBackButtonClick.bind(this);
  }

  componentWillMount() {
    if (this.props.onWillMount) {
      this.props.onWillMount();
    }
  }

  onBackButtonClick() {
    const {activeSubStepIndex} = this.state;

    if (activeSubStepIndex > 0) {
      this.setState(prevState => ({
        activeSubStepIndex: prevState.activeSubStepIndex - 1
      }));
    }
  }
  onNextButtonClick() {
    const {activeSubStepIndex} = this.state;
    if (activeSubStepIndex < 3) {
      this.setState(prevState => ({
        activeSubStepIndex: prevState.activeSubStepIndex + 1
      }));
    }
  }

  renderButtons() {
    const {activeSubStepIndex} = this.state;
    const {formSyncErrors} = this.props;
    let disabledButtonNext = true;
    switch (activeSubStepIndex) {
      case 0: {
        if (
          !formSyncErrors.title &&
          !formSyncErrors.serverName &&
          !formSyncErrors.datasource
        ) {
          disabledButtonNext = false;
        }
        break;
      }
      case 1: {
        disabledButtonNext = true;
        if (!formSyncErrors.axis) {
          disabledButtonNext = false;
        }
        break;
      }
      default:
        break;
    }
    return (
      <div className="DashboardMap__btn-container">
        <Button bsStyle="default" className="btn-cancel" onClick={this.close}>
          Cancel
        </Button>

        {activeSubStepIndex < 2 && (
          <Button
            bsStyle="primary"
            onClick={this.onNextButtonClick}
            className="pull-right"
            disabled={disabledButtonNext}
          >
            Next&nbsp;
            <Icon type="fa" name="angle-right" />
          </Button>
        )}

        {activeSubStepIndex === 2 && (
          <Button bsStyle="primary" type="submit" className="pull-right">
            Save&nbsp;
            <Icon type="fa" name="angle-right" />
          </Button>
        )}
        {activeSubStepIndex > 0 && (
          <Button
            bsStyle="default"
            disabled={activeSubStepIndex === 0}
            onClick={this.onBackButtonClick}
            className="DashboardMap__btn-back pull-right"
          >
            <Icon type="fa" name="angle-left" />
            &nbsp; Back
          </Button>
        )}
      </div>
    );
  }

  render() {
    const {activeSubStepIndex} = this.state;
    const {handleSubmit} = this.props;
    return (
      <Wizard className="DashboardMap">
        <Wizard.Body>
          <Wizard.Steps steps={<Steps stepIndex={activeSubStepIndex} />} />
          <Wizard.Row>
            <Wizard.Main>
              <form onSubmit={handleSubmit}>
                <div className="DashboardMap__data-container">
                  <div className="DashboardMap__data-contents">
                    <Wizard.Contents
                      key={0}
                      stepIndex={0}
                      subStepIndex={0}
                      activeStepIndex={0}
                      activeSubStepIndex={activeSubStepIndex}
                    >
                      <ChartFirstStepContent />
                    </Wizard.Contents>

                    <Wizard.Contents
                      key={1}
                      stepIndex={1}
                      subStepIndex={1}
                      activeStepIndex={1}
                      activeSubStepIndex={activeSubStepIndex}
                    >
                      SECOND STEP
                    </Wizard.Contents>
                    <Wizard.Contents
                      key={2}
                      stepIndex={2}
                      subStepIndex={2}
                      activeStepIndex={2}
                      activeSubStepIndex={activeSubStepIndex}
                    >
                      THIRD STEP
                    </Wizard.Contents>
                  </div>
                  {this.renderButtons()}
                </div>
              </form>
            </Wizard.Main>
          </Wizard.Row>
        </Wizard.Body>
      </Wizard>
    );
  }
}
DashboardMapFormBody.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onWillMount: PropTypes.func.isRequired
};

const DashboardMapForm = reduxForm({
  form: FORM_NAME
})(DashboardMapFormBody);

export default DashboardMapForm;
