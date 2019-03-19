import {connect} from "react-redux";
import {formValueSelector, getFormSyncErrors} from "redux-form";
import {pick, get, set} from "lodash";

import {fetchServerConfigList, getWidgetConfigChart} from "state/main/actions";

import DashboardBarChartForm from "ui/widgets/charts/bar-chart/components/DashboardBarChartForm";

const FORM_NAME = "form-dashboard-bar-chart";

const selector = formValueSelector(FORM_NAME);

const mapStateToProps = state => ({
  chart: "bar",
  datasource: selector(state, "datasource"),
  formSyncErrors: getFormSyncErrors(FORM_NAME)(state),
  axis: {rotated: selector(state, "axis.rotated")},
  columns: selector(state, "columns"),
  initialValues: {
    axis: {
      chart: "bar",
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
    bar: {width: 10},
    legend: {
      position: "bottom"
    }
  }
});

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
      bar: {
        width: parseInt(data.bar.width, 10)
      }
    };
    transformData.data = {
      type: "bar",
      json: [],
      keys: {
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
      set(transformData, "axis.x.tick.multiline", true);
      set(transformData, "axis.x.tick.multilineMax", 2);
      set(transformData, "axis.x.tick.rotate", 75);
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

const DashboardBarChartFormContainer = connect(
  mapStateToProps,
  mapDispatchToProps
)(DashboardBarChartForm);

export default DashboardBarChartFormContainer;
