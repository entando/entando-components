import React, {Component} from "react";
import PropTypes from "prop-types";
import {reduxForm} from "redux-form";
import {Button, Icon, Wizard} from "patternfly-react";

import ChartSteps from "ui/widgets/charts/common/components/ChartSteps";
import ChartFirstStepContent from "ui/widgets/charts/common/components/ChartFirstStepContent";
import ChartSecondStepContentContainer from "ui/widgets/charts/common/containers/ChartSecondStepContentContainer";
import ChartThirdStepContentContainer from "ui/widgets/charts/common/containers/ChartThirdStepContentContainer";

const data = {columns: [["data1", 30], ["data2", 120]]};

const FORM_NAME = "form-dashboard-donut-chart";

class DashboardDonutChartFormBody extends Component {
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
      <div className="DashboardDonutChart__btn-container">
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
            className="DashboardDonutChart__btn-back pull-right"
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
      <Wizard className="DashboardDonutChart">
        <Wizard.Body>
          <Wizard.Steps steps={<ChartSteps stepIndex={activeSubStepIndex} />} />
          <Wizard.Row>
            <Wizard.Main>
              <form onSubmit={handleSubmit}>
                <div className="DashboardDonutChart__data-container">
                  <div className="DashboardDonutChart__data-contents">
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
                      <ChartSecondStepContentContainer
                        type="DONUT_CHART"
                        data={data.columns}
                        labelChartPreview="Donut Chart"
                        formName={FORM_NAME}
                      />
                    </Wizard.Contents>
                    <Wizard.Contents
                      key={2}
                      stepIndex={2}
                      subStepIndex={2}
                      activeStepIndex={2}
                      activeSubStepIndex={activeSubStepIndex}
                    >
                      <ChartThirdStepContentContainer
                        type="DONUT_CHART"
                        data={data.columns}
                        labelChartPreview="Donut Chart"
                        formName={FORM_NAME}
                      />
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
DashboardDonutChartFormBody.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onWillMount: PropTypes.func.isRequired,
  invalid: PropTypes.bool,
  submitting: PropTypes.bool
};

DashboardDonutChartFormBody.defaultProps = {
  invalid: false,
  submitting: false
};
const DashboardDonutChartForm = reduxForm({
  form: FORM_NAME
})(DashboardDonutChartFormBody);

export default DashboardDonutChartForm;
