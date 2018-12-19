import {connect} from "react-redux";

import {fetchServerConfigList, fetchLanguages} from "state/main/actions";

import {getLanguages} from "state/main/selectors";

import DashboardTableForm from "ui/widgets/dashboard-table/Components/DashboardTableForm";

const mapStateToProps = state => ({
  initialValues: {
    allColumns: true
  },
  languages: getLanguages(state)
});

const mapDispatchToProps = dispatch => ({
  onWillMount: () => {
    dispatch(fetchServerConfigList());
    dispatch(fetchLanguages());
  },
  onSubmit: values => {
    console.log("values", values);
  }
});

const DashboardTableFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardTableForm);

export default DashboardTableFormContainer;
