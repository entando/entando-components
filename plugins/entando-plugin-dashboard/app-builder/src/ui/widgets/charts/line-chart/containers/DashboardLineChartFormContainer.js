import {connect} from "react-redux";
import {formValueSelector, getFormSyncErrors} from "redux-form";
import {get, set, pick} from "lodash";

import {fetchServerConfigList, getWidgetConfigChart} from "state/main/actions";

import DashboardLineChartForm from "ui/widgets/charts/line-chart/components/DashboardLineChartForm";

const selector = formValueSelector("form-dashboard-line-chart");

const mapStateToProps = state => ({
  datasource: selector(state, "datasource"),
  formSyncErrors: getFormSyncErrors("form-dashboard-line-chart")(state),
  axis: {rotated: selector(state, "axis.rotated")},
  chart: selector(state, "chart"),
  spline: selector(state, "spline"),
  columns: selector(state, "columns"),

  initialValues: {
    axis: {
      chart: "line",
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
    legend: {
      position: "bottom"
    }
  }
});

const mapDispatchToProps = (dispatch, ownProps) => ({
  onWillMount: () => {
    dispatch(fetchServerConfigList()).then(() => {
      dispatch(getWidgetConfigChart("form-dashboard-line-chart"));
    });
  },
  onSubmit: data => {
    const transformData = {...data};
    transformData.data = {
      json: [],
      keys: {
        //        x: get(data, "columns.x[0].key"),
        value: [
          ...get(data, "columns.x").map(m => m.key),
          ...get(data, "columns.y").map(m => m.key)
        ]
      }
    };
    /* https://c3js.org/reference.html#data-xs This option can be used if we want to show the data that has different x values.*/
    if (data.columns.x.length > 1) {
      const {x, y} = data.columns;
      transformData.data.xs = x.reduce((acc, item, index) => {
        acc[y[index].key] = item.key;
        return acc;
      }, {});
    } else {
      transformData.data.keys.x = get(data, "columns.x[0].key");
    }

    if (get(data, "axis.x.type") === "timeseries") {
      set(transformData, "axis.x.type", "timeseries");
      transformData.data.xFormat = get(data, "axis.x.tick.format");
    }

    transformData.columns = pick(transformData.columns, ["x", "y"]);
    console.log("Submit data ", {config: transformData});
    ownProps.onSubmit({config: JSON.stringify(transformData)});
  }
});

const DashboardLineChartFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardLineChartForm);

export default DashboardLineChartFormContainer;
