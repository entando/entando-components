import {connect} from "react-redux";
import {formValueSelector, getFormSyncErrors} from "redux-form";

import {fetchServerConfigList} from "state/main/actions";

import DashboardDonutChartForm from "ui/widgets/charts/donut-chart/components/DashboardDonutChartForm";

const mapStateToProps = state => {
  const formName = "form-dashboard-donut-chart";
  const selector = formValueSelector(formName);
  return {
    datasource: selector(state, "datasource"),
    formSyncErrors: getFormSyncErrors(formName)(state),
    initialValues: {
      axis: {
        x: {type: "indexed"},
        y2: {show: false}
      },

      legend: {
        position: "bottom"
      }
    }
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

const DashboardDonutChartFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardDonutChartForm);

export default DashboardDonutChartFormContainer;
