import {connect} from "react-redux";
import {formValueSelector, getFormSyncErrors} from "redux-form";
import {pick, omit} from "lodash";

import {fetchServerConfigList} from "state/main/actions";

import DashboardLineChartForm from "ui/widgets/charts/line-chart/components/DashboardLineChartForm";

const selector = formValueSelector("form-dashboard-line-chart");

const mapStateToProps = state => ({
  datasource: selector(state, "datasource"),
  formSyncErrors: getFormSyncErrors("form-dashboard-line-chart")(state),
  axis: {rotated: selector(state, "axis.rotated")},
  chart: selector(state, "chart"),
  initialValues: {
    axis: {
      chart: "line",
      rotated: false,
      x: {type: "indexed"},
      y2: {show: false}
    },

    legend: {
      position: "bottom"
    }
  }
});

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

const DashboardLineChartFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardLineChartForm);

export default DashboardLineChartFormContainer;
