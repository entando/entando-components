import {connect} from "react-redux";
import {formValueSelector, getFormSyncErrors} from "redux-form";
import {pick, get} from "lodash";

import {fetchServerConfigList, getWidgetConfigChart} from "state/main/actions";

import DashboardDonutChartForm from "ui/widgets/charts/donut-chart/components/DashboardDonutChartForm";

const FORM_NAME = "form-dashboard-donut-chart";

const mapStateToProps = state => {
  const selector = formValueSelector(FORM_NAME);
  const rotated = selector(state, "axis.rotated");

  return {
    chart: "donut",
    datasource: selector(state, "datasource"),
    axis: {rotated: rotated},
    formSyncErrors: getFormSyncErrors(FORM_NAME)(state),

    initialValues: {
      chart: "donut",
      axis: {
        rotated: false,
        x: {type: "indexed"},
        y2: {show: false}
      },
      size: {
        width: 300,
        height: 500
      },
      padding: {
        top: 50,
        right: 50,
        bottom: 50,
        left: 50
      },
      donut: {width: 50},
      legend: {
        position: "bottom"
      }
    }
  };
};

const mapDispatchToProps = (dispatch, ownProps) => ({
  onWillMount: () => {
    dispatch(fetchServerConfigList()).then(() => {
      dispatch(getWidgetConfigChart(FORM_NAME));
    });
  },
  onSubmit: data => {
    const transformData = {
      ...data,
      size: {
        width: parseInt(data.size.width, 10),
        height: parseInt(data.size.height, 10)
      },
      padding: {
        top: parseInt(data.padding.top, 10),
        right: parseInt(data.padding.right, 10),
        bottom: parseInt(data.padding.bottom, 10),
        left: parseInt(data.padding.left, 10)
      },
      donut: {
        width: parseInt(data.donut.width, 10)
      }
    };
    transformData.data = {
      type: "donut",
      json: [],
      keys: {
        value: [...get(data, "columns.x").map(m => m.key)]
      }
    };
    transformData.columns = pick(transformData.columns, ["x"]);
    console.log("Submit data ", {config: transformData});
    ownProps.onSubmit({config: JSON.stringify(transformData)});
  }
});

const DashboardDonutChartFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardDonutChartForm);

export default DashboardDonutChartFormContainer;
