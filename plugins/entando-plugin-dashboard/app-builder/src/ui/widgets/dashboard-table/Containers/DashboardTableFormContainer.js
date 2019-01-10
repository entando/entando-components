import {connect} from "react-redux";
import {formValueSelector} from "redux-form";

import {fetchServerConfigList, fetchLanguages} from "state/main/actions";

import {getLanguages} from "state/main/selectors";

import DashboardTableForm from "ui/widgets/dashboard-table/Components/DashboardTableForm";

const selector = formValueSelector("form-dashboard-table");

const mapStateToProps = state => ({
  initialValues: {
    allColumns: true
  },
  languages: getLanguages(state),
  datasource: selector(state, "datasource")
});

const mapDispatchToProps = (dispatch, ownProps) => ({
  onWillMount: () => {
    dispatch(fetchServerConfigList());
    dispatch(fetchLanguages());
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
