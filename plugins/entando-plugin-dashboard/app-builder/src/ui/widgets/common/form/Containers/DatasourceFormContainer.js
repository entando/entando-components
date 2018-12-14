import {connect} from "react-redux";

import {getDatasourceList} from "state/main/selectors";

import DatasourceForm from "ui/widgets/common/form/Components/DatasourceForm";

const mapStateToProps = state => ({
  datasources: getDatasourceList(state)
});

const DatasourceFormContainer = connect(
  mapStateToProps,
  null
)(DatasourceForm);

export default DatasourceFormContainer;
