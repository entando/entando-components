import React, {Component} from "react";
import PropTypes from "prop-types";
import {reduxForm} from "redux-form";

import Stepper from "ui/widgets/common/components/Stepper";
import ChartFirstStepContent from "ui/widgets/charts/common/components/ChartFirstStepContent";
import ChartSecondStepContent from "ui/widgets/charts/common/components/ChartSecondStepContent";
import ChartThirdStepContent from "ui/widgets/charts/common/components/ChartThirdStepContent";

const data = {columns: [["data1", 10, 30, 10, 20, 40, 50]]};

const FORM_NAME = "form-dashboard-line-chart";
let TYPE_CHART = "LINE_CHART";
const CHART_PREVIEW = "Line Chart";

class DashboardLineChartFormBody extends Component {
  componentWillMount() {
    this.props.onWillMount();
  }

  render() {
    const {
      formSyncErrors,
      axis: {rotated},
      spline,
      columns
    } = this.props;

    const validateSteps = [false, false, false];
    if (
      !formSyncErrors.title &&
      !formSyncErrors.serverName &&
      !formSyncErrors.datasource
    ) {
      validateSteps[0] = true;
    }
    if (!formSyncErrors.axis && !formSyncErrors.columns) {
      validateSteps[1] = true;
    }
    if (columns) {
      if (Object.keys(formSyncErrors).length > 0) {
        validateSteps[1] = false;
      } else if (
        (columns.x && columns.x.length === 0) ||
        (columns.y && columns.y.length === 0)
      ) {
        validateSteps[1] = false;
      } else if (columns.x && columns.y) {
        if (columns.x.length > columns.y.length) {
          validateSteps[1] = false;
        } else {
          validateSteps[1] = true;
        }
      } else {
        validateSteps[1] = false;
      }
    }

    spline ? (TYPE_CHART = "SPLINE_CHART") : (TYPE_CHART = "LINE_CHART");
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
  spline: PropTypes.bool
};

DashboardLineChartFormBody.defaultProps = {
  spline: false
};
const DashboardLineChartForm = reduxForm({
  form: FORM_NAME
})(DashboardLineChartFormBody);

export default DashboardLineChartForm;
