import {
  mapStateToProps,
  mapDispatchToProps
} from "ui/widgets/charts/pie-chart/containers/DashboardPieChartFormContainer";

import {
  fetchServerConfigList,
  getWidgetConfigChart,
  gotoConfigurationPage
} from "state/main/actions";

import {CONFIG_PIE_CHART} from "mocks/dashboardConfigs";

jest.mock("state/main/actions");

fetchServerConfigList.mockImplementation(() => Promise.resolve({}));

const ownProps = {
  onSubmit: jest.fn()
};

describe("DashboardPieChartFormContainer", () => {
  let container;

  describe("mapStateToProps", () => {
    it("maps properties state in DashboardGaugeChartForm", () => {
      container = mapStateToProps({});
      expect(container).toHaveProperty("chart", "pie");
      expect(container).toHaveProperty("datasource");
      expect(container).toHaveProperty("formSyncErrors");
      expect(container).toHaveProperty("initialValues");
      expect(container).toHaveProperty("initialValues.axis");
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
      expect(container).toHaveProperty("initialValues.pie.expand");
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
          "form-dashboard-pie-chart"
        );
      });
      done();
    });

    it("should call onSubmit dispatch ownProps.onSubmit", () => {
      container.onSubmit(CONFIG_PIE_CHART.config);
      expect(ownProps.onSubmit).toHaveBeenCalled();
    });

    it("should call onCancel dispatch gotoConfigurationPage", () => {
      container.onCancel();
      expect(gotoConfigurationPage).toHaveBeenCalled();
    });
  });
});
