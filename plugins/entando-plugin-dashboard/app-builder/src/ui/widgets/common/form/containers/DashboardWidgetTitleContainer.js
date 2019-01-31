import {connect} from "react-redux";

import {fetchLanguages} from "state/main/actions";

import {getLanguages} from "state/main/selectors";

import DashboardWidgetTitle from "ui/widgets/common/form/components/DashboardWidgetTitle";

const mapStateToProps = state => ({
  languages: getLanguages(state)
});

const mapDispatchToProps = (dispatch, ownProps) => ({
  onWillMount: () => {
    dispatch(fetchLanguages());
  }
});

const DashboardWidgetTitleContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardWidgetTitle);
export default DashboardWidgetTitleContainer;
