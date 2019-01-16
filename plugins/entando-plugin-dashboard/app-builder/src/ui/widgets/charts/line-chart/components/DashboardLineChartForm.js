import React, {Component} from "react";
import PropTypes from "prop-types";
import {reduxForm} from "redux-form";
import {Button, Icon, Wizard} from "patternfly-react";

import ChartSteps from "ui/widgets/charts/common/components/ChartSteps";
import ChartFirstStepContent from "ui/widgets/charts/common/components/ChartFirstStepContent";
import ChartSecondStepContentContainer from "ui/widgets/charts/line-chart/containers/ChartSecondStepContentContainer";

const data = {columns: [["data1", 10, 30, 10, 20, 40, 50]]};

class DashboardLineChartFormBody extends Component {
  constructor(props) {
    super(props);
    this.state = {
      activeSubStepIndex: 1
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

  render() {
    const {activeSubStepIndex} = this.state;
    const {handleSubmit} = this.props;
    return (
      <Wizard className="DashboardLineChart">
        <Wizard.Body>
          <Wizard.Steps steps={<ChartSteps stepIndex={activeSubStepIndex} />} />
          <Wizard.Row>
            <Wizard.Main>
              <form onSubmit={handleSubmit}>
                <div className="DashboardLineChart__data-container">
                  <div className="DashboardLineChart__data-contents">
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
                        type="LINE_CHART"
                        data={data.columns}
                        labelChartPreview="Line Chart"
                      />
                    </Wizard.Contents>
                    <Wizard.Contents
                      key={2}
                      stepIndex={2}
                      subStepIndex={2}
                      activeStepIndex={2}
                      activeSubStepIndex={activeSubStepIndex}
                    >
                      TERZO STEP
                    </Wizard.Contents>
                  </div>

                  <div className="DashboardLineChart__btn-container">
                    <Button
                      bsStyle="default"
                      className="btn-cancel"
                      onClick={this.close}
                    >
                      Cancel
                    </Button>

                    {activeSubStepIndex < 2 && (
                      <Button
                        bsStyle="primary"
                        onClick={this.onNextButtonClick}
                        className="pull-right"
                      >
                        Next&nbsp;
                        <Icon type="fa" name="angle-right" />
                      </Button>
                    )}

                    {activeSubStepIndex === 2 && (
                      <Button
                        bsStyle="primary"
                        onClick={this.close}
                        className="pull-right"
                      >
                        Save&nbsp;
                        <Icon type="fa" name="angle-right" />
                      </Button>
                    )}
                    {activeSubStepIndex > 0 && (
                      <Button
                        bsStyle="default"
                        disabled={activeSubStepIndex === 0}
                        onClick={this.onBackButtonClick}
                        className="DashboardLineChart__btn-back pull-right"
                      >
                        <Icon type="fa" name="angle-left" />
                        &nbsp; Back
                      </Button>
                    )}
                  </div>
                </div>
              </form>
            </Wizard.Main>
          </Wizard.Row>
        </Wizard.Body>
      </Wizard>
    );
  }
}
DashboardLineChartFormBody.propTypes = {
  handleSubmit: PropTypes.func.isRequired,
  onWillMount: PropTypes.func.isRequired,
  invalid: PropTypes.bool,
  submitting: PropTypes.bool
};

DashboardLineChartFormBody.defaultProps = {
  invalid: false,
  submitting: false
};
const DashboardLineChartForm = reduxForm({
  form: "form-dashboard-line-chart"
})(DashboardLineChartFormBody);

export default DashboardLineChartForm;
