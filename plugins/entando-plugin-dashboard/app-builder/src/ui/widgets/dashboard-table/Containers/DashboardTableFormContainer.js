import {connect} from "react-redux";
import {fetchServerConfigList} from "state/main/actions";

import DashboardTableForm from "ui/widgets/dashboard-table/Components/DashboardTableForm";

const mapStateToProps = () => ({
  initialValues: {
    allColumns: true
  }
});

const mapDispatchToProps = dispatch => ({
  onWillMount: () => {
    dispatch(fetchServerConfigList());
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
