import React, {Component} from "react";
import PropTypes from "prop-types";

import {Button, Icon, Wizard} from "patternfly-react";

import Steps from "ui/widgets/common/components/Steps";

class Stepper extends Component {
  constructor(props) {
    super(props);
    this.state = {
      activeSubStepIndex: 0
    };
    this.onNextButtonClick = this.onNextButtonClick.bind(this);
    this.onBackButtonClick = this.onBackButtonClick.bind(this);
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
    const {validateSteps} = this.props;
    const disabledButtonNext = !validateSteps[activeSubStepIndex];
    return (
      <div className="Stepper__btn-container">
        <Button
          bsStyle="default"
          className="btn-cancel"
          onClick={this.props.onCancel}
        >
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
            className="Stepper__btn-back pull-right"
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
    const {handleSubmit, step1, step2, step3} = this.props;
    return (
      <Wizard className="Stepper">
        <Wizard.Body>
          <Wizard.Steps steps={<Steps stepIndex={activeSubStepIndex} />} />
          <Wizard.Row>
            <Wizard.Main>
              <form onSubmit={handleSubmit}>
                <div className="Stepper__data-container">
                  <div className="Stepper__data-contents">
                    <Wizard.Contents
                      key={0}
                      stepIndex={0}
                      subStepIndex={0}
                      activeStepIndex={0}
                      activeSubStepIndex={activeSubStepIndex}
                    >
                      {step1}
                    </Wizard.Contents>

                    <Wizard.Contents
                      key={1}
                      stepIndex={1}
                      subStepIndex={1}
                      activeStepIndex={1}
                      activeSubStepIndex={activeSubStepIndex}
                    >
                      {step2}
                    </Wizard.Contents>
                    <Wizard.Contents
                      key={2}
                      stepIndex={2}
                      subStepIndex={2}
                      activeStepIndex={2}
                      activeSubStepIndex={activeSubStepIndex}
                    >
                      {step3}
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

Stepper.propTypes = {
  onCancel: PropTypes.func.isRequired,
  handleSubmit: PropTypes.func,
  step1: PropTypes.node,
  step2: PropTypes.node,
  step3: PropTypes.node,
  validateSteps: PropTypes.arrayOf(PropTypes.bool).isRequired
};

Stepper.defaultProps = {
  handleSubmit: () => {},
  step1: null,
  step2: null,
  step3: null
};
export default Stepper;
