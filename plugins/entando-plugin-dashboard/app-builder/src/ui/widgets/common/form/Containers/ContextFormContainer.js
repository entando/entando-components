import {connect} from "react-redux";

import {getContextList} from "state/main/selectors";

import ContextForm from "ui/widgets/common/form/Components/ContextForm";

const mapStateToProps = state => ({
  contexts: getContextList(state)
});

const ContextFormContainer = connect(
  mapStateToProps,
  null
)(ContextForm);

export default ContextFormContainer;
