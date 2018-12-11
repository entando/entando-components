import {connect} from "react-redux";
import {initialize} from "redux-form";

import TableListDevicesForm from "ui/widgets/table-list-devices/Components/TableListDevicesForm";

const mapDispatchToProps = dispatch => ({
  onWillMount: () => {
    dispatch(
      initialize("form-list-devices", {
        allColumns: true
      })
    );
  }
});

const TableListDevicesFormContainer = connect(
  null,
  mapDispatchToProps
)(TableListDevicesForm);

export default TableListDevicesFormContainer;
