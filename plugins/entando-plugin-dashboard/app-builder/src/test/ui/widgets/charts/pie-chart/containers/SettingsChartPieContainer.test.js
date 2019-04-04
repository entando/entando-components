import {mapStateToProps} from "ui/widgets/charts/pie-chart/containers/SettingsChartPieContainer";

const ownProps = {
  formName: "form-dashboard-pie-chart"
};

jest.mock("state/main/selectors");

describe("SettingsChartPieContainer", () => {
  let container;
  describe("mapStateToProps", () => {
    it("maps properties state in SettingsChartPie", () => {
      container = mapStateToProps(null, ownProps);
      expect(container).toHaveProperty("datasourceSelected");
      expect(container).toHaveProperty("optionColumns");
      expect(container).toHaveProperty("optionColumnXSelected");
      expect(container).toHaveProperty("optionColumnYSelected");
    });
  });
});
