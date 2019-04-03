import React from "react";
import "test/enzyme-init";
import {shallow} from "enzyme";

import {DashboardLineChartFormBody} from "ui/widgets/charts/line-chart/components/DashboardLineChartForm";
import Stepper from "ui/widgets/common/components/Stepper";

const props = {
  handleSubmit: jest.fn(),
  onWillMount: jest.fn(),
  onCancel: jest.fn(),
  spline: false,
  axis: {
    rotated: false
  },
  formSyncErrors: {}
};

describe("DashboardLineChartForm", () => {
  let component;
  beforeEach(() => {
    component = shallow(<DashboardLineChartFormBody {...props} />);
  });

  it("renders without crashing", () => {
    expect(component.exists()).toBe(true);
  });

  it("contain one Stepper ", () => {
    expect(component.find(Stepper)).toHaveLength(1);
  });

  it("will call onWillMount on componentWillMount", () => {
    expect(props.onWillMount).toHaveBeenCalled();
  });

  it("set spline props to true", () => {
    component.setProps({spline: true});
    const stepper = component.find(Stepper);
    const third = stepper.prop("step3");
    expect(third.props.typeChart).toBe("SPLINE_CHART");
  });

  it("validateSteps", () => {
    component.setProps({
      formSyncErrors: {
        title: "title",
        serverName: "server",
        datasource: "ds"
      }
    });
    const stepper = component.find(Stepper);
    expect(stepper.prop("validateSteps")).toEqual(
      expect.arrayContaining([false, true, false])
    );
  });

  it("validateSteps with columns props", () => {
    component.setProps({
      columns: [{key: 1}],
      formSyncErrors: {}
    });
    const stepper = component.find(Stepper);
    expect(stepper.prop("validateSteps")).toEqual(
      expect.arrayContaining([false, true, false])
    );
  });
});
