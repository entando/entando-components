import {connect} from "react-redux";
import {formValueSelector, getFormSyncErrors} from "redux-form";
import {pick, omit} from "lodash";

import {fetchServerConfigList} from "state/main/actions";

import DashboardGaugeChartForm from "ui/widgets/charts/gauge-chart/components/DashboardGaugeChartForm";

const mapStateToProps = state => {
  const formName = "form-dashboard-gauge-chart";
  const selector = formValueSelector(formName);

  return {
    initialValues: {
      chart: "gauge",
      axis: {
        rotated: false,
        x: {type: "indexed"},
        y2: {show: false}
      },

      legend: {
        position: "bottom"
      }
    },
    chart: selector(state, "chart"),
    datasource: selector(state, "datasource"),
    formSyncErrors: getFormSyncErrors(formName)(state)
  };
};

const mapDispatchToProps = (dispatch, ownProps) => ({
  onWillMount: () => {
    dispatch(fetchServerConfigList());
  },
  onSubmit: data => {
    const transformData = {
      ...pick(data, ["datasource", "title", "serverName"])
    };
    transformData.configChart = {
      ...omit(data, ["datasource", "title", "serverName"])
    };

    //console.log("Submit data ", transformData);
    ownProps.onSubmit();
  }
});

const DashboardGaugeChartFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardGaugeChartForm);

export default DashboardGaugeChartFormContainer;
