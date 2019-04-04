import React from "react";
import "test/enzyme-init";
import {shallow} from "enzyme";

import DashboardWidgetConfigPage from "ui/widgets/common/components/DashboardWidgetConfigPage";

import DashboardDonutChart from "ui/widgets/charts/donut-chart/components/DashboardDonutChart";
import DashboardDonutChartFormContainer from "ui/widgets/charts/donut-chart/containers/DashboardDonutChartFormContainer";

const onSubmit = jest.fn();

describe("DashboardDonutChart", () => {
  let component;
  beforeEach(() => {
    component = shallow(<DashboardDonutChart onSubmit={onSubmit} />);
  });

  it("renders without crashing", () => {
    expect(component.exists()).toBe(true);
    expect(component.find(DashboardWidgetConfigPage)).toHaveLength(1);
    expect(component.find(DashboardDonutChartFormContainer)).toHaveLength(1);
  });
});
