import {mapStateToProps} from "ui/widgets/charts/gauge-chart/containers/SettingsChartGaugeContainer";

const ownProps = {
  formName: "form-dashboard-gauge-chart"
};

jest.mock("state/main/selectors");

describe("SettingsChartGaugeContainer", () => {
  let container;
  describe("mapStateToProps", () => {
    it("maps properties state in SettingsChartGauge", () => {
      container = mapStateToProps(null, ownProps);
      expect(container).toHaveProperty("datasourceSelected");
      expect(container).toHaveProperty("optionColumns");
      expect(container).toHaveProperty("optionColumnXSelected");
      expect(container).toHaveProperty("optionColumnYSelected");
    });
  });
});
