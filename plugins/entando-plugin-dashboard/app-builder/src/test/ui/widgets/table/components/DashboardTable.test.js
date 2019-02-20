import React from "react";
import "test/enzyme-init";
import {shallow} from "enzyme";

import DashboardTable from "ui/widgets/table/components/DashboardTable";
import DashboardWidgetConfigPage from "ui/widgets/common/components/DashboardWidgetConfigPage";
import DashboardTableFormContainer from "ui/widgets/table/containers/DashboardTableFormContainer";

const onSubmit = jest.fn();

describe("DashboardTable", () => {
  let component;
  beforeEach(() => {
    component = shallow(<DashboardTable onSubmit={onSubmit} />);
  });

  it("renders without crashing", () => {
    expect(component.exists()).toBe(true);
    expect(component.find(DashboardWidgetConfigPage)).toHaveLength(1);
    expect(component.find(DashboardTableFormContainer)).toHaveLength(1);
  });
});
