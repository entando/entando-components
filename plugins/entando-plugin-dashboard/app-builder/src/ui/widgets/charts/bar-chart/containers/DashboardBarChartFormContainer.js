import {connect} from "react-redux";
import {formValueSelector, getFormSyncErrors} from "redux-form";
import {pick, omit} from "lodash";

import {fetchServerConfigList} from "state/main/actions";

import DashboardBarChartForm from "ui/widgets/charts/bar-chart/components/DashboardBarChartForm";

const selector = formValueSelector("form-dashboard-bar-chart");

const mapStateToProps = state => ({
  datasource: selector(state, "datasource"),
  formSyncErrors: getFormSyncErrors("form-dashboard-bar-chart")(state),
  axis: {rotated: selector(state, "axis.rotated")},
  chart: selector(state, "chart"),
  initialValues: {
    axis: {
      chart: "bar",
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

const DashboardBarChartFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardBarChartForm);

export default DashboardBarChartFormContainer;
