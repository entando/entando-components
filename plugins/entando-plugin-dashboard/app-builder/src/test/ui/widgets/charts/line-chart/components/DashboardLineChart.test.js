import React from "react";
import "test/enzyme-init";
import {shallow} from "enzyme";

import DashboardWidgetConfigPage from "ui/widgets/common/components/DashboardWidgetConfigPage";

import DashboardLineChart from "ui/widgets/charts/line-chart/components/DashboardLineChart";
import DashboardLineChartFormContainer from "ui/widgets/charts/line-chart/containers/DashboardLineChartFormContainer";

const onSubmit = jest.fn();

describe("DashboardLineChart", () => {
  let component;
  beforeEach(() => {
    component = shallow(<DashboardLineChart onSubmit={onSubmit} />);
  });

  it("renders without crashing", () => {
    expect(component.exists()).toBe(true);
    expect(component.find(DashboardWidgetConfigPage)).toHaveLength(1);
    expect(component.find(DashboardLineChartFormContainer)).toHaveLength(1);
  });
});
