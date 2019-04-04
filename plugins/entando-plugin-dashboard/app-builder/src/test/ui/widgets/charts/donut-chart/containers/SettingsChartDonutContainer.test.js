import {mapStateToProps} from "ui/widgets/charts/donut-chart/containers/SettingsChartDonutContainer";

const ownProps = {
  formName: "form-dashboard-donut-chart"
};

jest.mock("state/main/selectors");

describe("SettingsChartDonutContainer", () => {
  let container;
  describe("mapStateToProps", () => {
    it("maps properties state in SettingsChartDonut", () => {
      container = mapStateToProps(null, ownProps);
      expect(container).toHaveProperty("datasourceSelected");
      expect(container).toHaveProperty("optionColumns");
      expect(container).toHaveProperty("optionColumnXSelected");
      expect(container).toHaveProperty("optionColumnYSelected");
    });
  });
});
