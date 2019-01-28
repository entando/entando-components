import {connect} from "react-redux";
import {formValueSelector, getFormSyncErrors} from "redux-form";
import {pick, omit} from "lodash";

import {fetchServerConfigList} from "state/main/actions";

import DashboardDonutChartForm from "ui/widgets/charts/donut-chart/components/DashboardDonutChartForm";

const mapStateToProps = state => {
  const formName = "form-dashboard-donut-chart";
  const selector = formValueSelector(formName);
  const rotated = selector(state, "axis.rotated");

  return {
    initialValues: {
      chart: "donut",
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
    axis: {rotated: rotated},
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

const DashboardDonutChartFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardDonutChartForm);

export default DashboardDonutChartFormContainer;
