import {connect} from "react-redux";
import {formValueSelector} from "redux-form";
import jsonTransform from "helpers/jsonTransform";

import {fetchServerConfigList, getTableWidgetConfig} from "state/main/actions";

import DashboardTableForm from "ui/widgets/table/components/DashboardTableForm";

const selector = formValueSelector("form-dashboard-table");

const mapStateToProps = state => {
  return {
    initialValues: {
      allColumns: true,
      options: {
        downlodable: true,
        filtrable: true
      }
    },
    datasource: selector(state, "datasource")
  };
};

const mapDispatchToProps = (dispatch, ownProps) => ({
  onWillMount: () => {
    dispatch(fetchServerConfigList()).then(() => {
      dispatch(getTableWidgetConfig("form-dashboard-table"));
    });
  },
  onSubmit: data => {
    const transformedData = {...data};
    transformedData.columns = Object.keys(data.columns)
      .filter(f => !data.columns[f].hidden)
      .reduce((acc, key) => {
        acc[key] = transformedData.columns[key].label;
        return acc;
      }, {});
    transformedData.allColumns = data.allColumns ? "true" : "false";
    transformedData.options.downlodable = data.options.downlodable
      ? "true"
      : "false";
    transformedData.options.filtrable = data.options.filtrable
      ? "true"
      : "false";
    ownProps.onSubmit(jsonTransform(transformedData));
  }
});

const DashboardTableFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardTableForm);

export default DashboardTableFormContainer;
