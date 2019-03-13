import React, {Component} from "react";
import PropTypes from "prop-types";
import {reduxForm} from "redux-form";

import Stepper from "ui/widgets/common/components/Stepper";
import ChartFirstStepContent from "ui/widgets/charts/common/components/ChartFirstStepContent";
import ChartSecondStepContent from "ui/widgets/charts/common/components/ChartSecondStepContent";
import ChartThirdStepContent from "ui/widgets/charts/common/components/ChartThirdStepContent";

const data = {columns: [["data1", 10, 30, 10, 20, 40, 50]]};

const FORM_NAME = "form-dashboard-line-chart";
const TYPE_CHART = "LINE_CHART";
const CHART_PREVIEW = "Line Chart";

class DashboardLineChartFormBody extends Component {
  componentWillMount() {
    this.props.onWillMount();
  }

  render() {
    const {
      formSyncErrors,
      axis: {rotated}
    } = this.props;

    const validateSteps = [false, false, false];
    if (
      !formSyncErrors.title &&
      !formSyncErrors.serverName &&
      !formSyncErrors.datasource
    ) {
      validateSteps[0] = true;
    }
    if (!formSyncErrors.axis) {
      validateSteps[1] = true;
    }

    return (
      <Stepper
        handleSubmit={this.props.handleSubmit}
        validateSteps={validateSteps}
        step1={<ChartFirstStepContent formName={FORM_NAME} />}
        step2={
          <ChartSecondStepContent
            typeChart={TYPE_CHART}
            data={data.columns}
            labelChartPreview={CHART_PREVIEW}
            formName={FORM_NAME}
            rotated={rotated}
          />
        }
        step3={
          <ChartThirdStepContent
            typeChart={TYPE_CHART}
            data={data.columns}
            labelChartPreview={CHART_PREVIEW}
            formName={FORM_NAME}
            rotated={rotated}
          />
        }
      />
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
  form: FORM_NAME
})(DashboardLineChartFormBody);

export default DashboardLineChartForm;
