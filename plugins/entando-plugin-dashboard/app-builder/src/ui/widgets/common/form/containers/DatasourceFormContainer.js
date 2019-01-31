import {connect} from "react-redux";

import {fetchDatasourceColumns} from "state/main/actions";

import {getDatasourceList} from "state/main/selectors";

import DatasourceForm from "ui/widgets/common/form/components/DatasourceForm";

const mapStateToProps = (state, ownProps) => ({
  datasources: getDatasourceList(state),
  nameFieldArray: ownProps.nameFieldArray
});

const mapDispatchToProps = (dispatch, ownProps) => ({
  onChange: value => {
    dispatch(fetchDatasourceColumns(ownProps.formName, "serverName", value));
  }
});

const DatasourceFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DatasourceForm);

export default DatasourceFormContainer;
