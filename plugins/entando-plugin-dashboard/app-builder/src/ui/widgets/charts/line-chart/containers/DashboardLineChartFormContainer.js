import {connect} from "react-redux";
import {formValueSelector, getFormSyncErrors} from "redux-form";

import {fetchServerConfigList} from "state/main/actions";

import DashboardLineChartForm from "ui/widgets/charts/line-chart/components/DashboardLineChartForm";

const selector = formValueSelector("form-dashboard-line-chart");

const mapStateToProps = state => ({
  datasource: selector(state, "datasource"),
  formSyncErrors: getFormSyncErrors("form-dashboard-line-chart")(state)
});

const mapDispatchToProps = (dispatch, ownProps) => ({
  onWillMount: () => {
    dispatch(fetchServerConfigList());
  },
  onSubmit: data => {
    const transformedData = {...data};
    transformedData.allColumns = data.allColumns ? "true" : "false";
    ownProps.onSubmit();
  }
});

const DashboardLineChartFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardLineChartForm);

export default DashboardLineChartFormContainer;
