import {connect} from "react-redux";
import {formValueSelector} from "redux-form";

import {fetchServerConfigList, getWidgetConfig} from "state/main/actions";

import DashboardTableForm from "ui/widgets/table/components/DashboardTableForm";

const selector = formValueSelector("form-dashboard-table");

export const mapStateToProps = state => ({
  initialValues: {
    allColumns: true,
    options: {
      downlodable: true,
      filtrable: true
    }
  },
  datasource: selector(state, "datasource")
});

export const mapDispatchToProps = (dispatch, ownProps) => ({
  onWillMount: () => {
    dispatch(fetchServerConfigList()).then(() => {
      dispatch(getWidgetConfig("form-dashboard-table"));
    });
  },
  onSubmit: data => {
    const transformedData = {...data};
    ownProps.onSubmit({config: JSON.stringify(transformedData)});
  }
});

const DashboardTableFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardTableForm);

export default DashboardTableFormContainer;
