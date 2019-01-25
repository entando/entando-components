import {connect} from "react-redux";
import {formValueSelector, getFormSyncErrors} from "redux-form";

import {fetchServerConfigList} from "state/main/actions";

import DashboardPieChartForm from "ui/widgets/charts/pie-chart/components/DashboardPieChartForm";

const mapStateToProps = state => {
  const formName = "form-dashboard-pie-chart";
  const selector = formValueSelector(formName);

  return {
    initialValues: {
      chart: "pie",
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
    const {
      title,
      serverName,
      datasource,
      axis,
      columns,
      size,
      padding,
      iteraction,
      legend
    } = data;
    const transformData = {
      serverName,
      datasource,
      title
    };
    transformData.configChart = {
      axis,
      columns,
      size,
      padding,
      iteraction,
      legend
    };

    console.log("Submit data ", transformData);
    //ownProps.onSubmit();
  }
});

const DashboardPieChartFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardPieChartForm);

export default DashboardPieChartFormContainer;
