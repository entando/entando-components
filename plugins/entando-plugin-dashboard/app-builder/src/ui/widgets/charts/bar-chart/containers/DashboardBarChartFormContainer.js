import {connect} from "react-redux";
import {formValueSelector, getFormSyncErrors} from "redux-form";

import {fetchServerConfigList} from "state/main/actions";

import DashboardBarChartForm from "ui/widgets/charts/bar-chart/components/DashboardBarChartForm";

const selector = formValueSelector("form-dashboard-bar-chart");

const mapStateToProps = state => ({
  datasource: selector(state, "datasource"),
  formSyncErrors: getFormSyncErrors("form-dashboard-bar-chart")(state),
  initialValues: {
    axis: {
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

const DashboardBarChartFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardBarChartForm);

export default DashboardBarChartFormContainer;
