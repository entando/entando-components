import {connect} from "react-redux";
import {formValueSelector, getFormSyncErrors} from "redux-form";
import {pick, get} from "lodash";

import {fetchServerConfigList, getWidgetConfigChart} from "state/main/actions";

import DashboardGaugeChartForm from "ui/widgets/charts/gauge-chart/components/DashboardGaugeChartForm";
const FORM_NAME = "form-dashboard-gauge-chart";
const selector = formValueSelector(FORM_NAME);

const mapStateToProps = state => {
  return {
    datasource: selector(state, "datasource"),
    formSyncErrors: getFormSyncErrors(FORM_NAME)(state),
    chart: selector(state, "chart"),
    columns: selector(state, "columns"),

    initialValues: {
      chart: "gauge",
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
      gauge: {
        min: 0,
        max: 100
      },

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
      gauge: {
        min: parseInt(data.gauge.min, 10),
        max: parseInt(data.gauge.max, 10)
      }
    };
    transformData.data = {
      type: "gauge",
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

const DashboardGaugeChartFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardGaugeChartForm);

export default DashboardGaugeChartFormContainer;
