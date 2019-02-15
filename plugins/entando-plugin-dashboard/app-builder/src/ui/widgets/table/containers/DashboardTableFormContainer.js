import {connect} from "react-redux";
import {formValueSelector} from "redux-form";

import {fetchServerConfigList} from "state/main/actions";

import DashboardTableForm from "ui/widgets/table/components/DashboardTableForm";

const selector = formValueSelector("form-dashboard-table");

const mapStateToProps = state => ({
  initialValues: {
    allColumns: true
  },
  datasource: selector(state, "datasource")
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

const DashboardTableFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardTableForm);

export default DashboardTableFormContainer;
