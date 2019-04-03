import {
  mapStateToProps,
  mapDispatchToProps
} from "ui/widgets/charts/line-chart/containers/DashboardLineChartFormContainer";

import {
  fetchServerConfigList,
  getWidgetConfigChart,
  gotoConfigurationPage
} from "state/main/actions";

import {CONFIG_CHART} from "mocks/dashboardConfigs";

jest.mock("state/main/actions");

fetchServerConfigList.mockImplementation(() => Promise.resolve({}));

const ownProps = {
  onSubmit: jest.fn()
};

describe("DashboardLineChartFormContainer", () => {
  let container;

  describe("mapStateToProps", () => {
    it("maps properties state in DashboardLineChartForm", () => {
      container = mapStateToProps({});
      expect(container).toHaveProperty("chart");
      expect(container).toHaveProperty("datasource");
      expect(container).toHaveProperty("formSyncErrors");
      expect(container).toHaveProperty("axis");
      expect(container).toHaveProperty("axis.rotated");
      expect(container).toHaveProperty("spline");
      expect(container).toHaveProperty("columns");
      expect(container).toHaveProperty("initialValues");
      expect(container).toHaveProperty("initialValues.axis");
      expect(container).toHaveProperty("initialValues.axis.chart");
      expect(container).toHaveProperty("initialValues.axis.rotated");
      expect(container).toHaveProperty("initialValues.axis.x.type");
      expect(container).toHaveProperty("initialValues.axis.y2.show");
      expect(container).toHaveProperty("initialValues.size");
      expect(container).toHaveProperty("initialValues.size.width");
      expect(container).toHaveProperty("initialValues.size.height");
      expect(container).toHaveProperty("initialValues.padding");
      expect(container).toHaveProperty("initialValues.padding.top");
      expect(container).toHaveProperty("initialValues.padding.right");
      expect(container).toHaveProperty("initialValues.padding.bottom");
      expect(container).toHaveProperty("initialValues.padding.left");
      expect(container).toHaveProperty("initialValues.legend");
      expect(container).toHaveProperty("initialValues.legend.position");
    });
  });

  describe("mapDispatchToProps", () => {
    const dispatchMock = jest.fn(args => args);
    beforeEach(() => {
      jest.clearAllMocks();
      container = mapDispatchToProps(dispatchMock, ownProps);
    });

    it("should map the correct function properties", () => {
      expect(container.onWillMount).toBeDefined();
      expect(container.onSubmit).toBeDefined();
      expect(container.onCancel).toBeDefined();
    });

    it("should call onWillMount and dispatch action fetchServerConfigList and getTableWidgetConfig", done => {
      container.onWillMount();
      expect(fetchServerConfigList).toHaveBeenCalled();
      fetchServerConfigList().then(() => {
        expect(getWidgetConfigChart).toHaveBeenCalledWith(
          "form-dashboard-line-chart"
        );
      });
      done();
    });

    it("should call onSubmit dispatch ownProps.onSubmit", () => {
      container.onSubmit(CONFIG_CHART.config);
      expect(ownProps.onSubmit).toHaveBeenCalled();
    });

    it("should call onSubmit dispatch ownProps.onSubmit with multi columns ", () => {
      const CONFIG_CHART_MULTI_COLUMNS = {...CONFIG_CHART.config};
      CONFIG_CHART_MULTI_COLUMNS.columns.x.push({
        id: 2,
        key: "timestamp1",
        value: "timestamp1",
        selected: true
      });
      CONFIG_CHART_MULTI_COLUMNS.columns.y.push({
        id: 4,
        key: "temperature1",
        value: "temperature1",
        selected: true
      });
      container.onSubmit(CONFIG_CHART_MULTI_COLUMNS);
      expect(ownProps.onSubmit).toHaveBeenCalled();
    });

    it("should call onCancel dispatch gotoConfigurationPage", () => {
      container.onCancel();
      expect(gotoConfigurationPage).toHaveBeenCalled();
    });
  });
});
