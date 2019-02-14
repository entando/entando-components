import React from "react";
import DashboardConfigDatasourceStatus from "ui/dashboard-config/common/components/DashboardConfigDatasourceStatus";
import {Label, Button} from "patternfly-react";
import {configure, shallow} from "enzyme";
import Adapter from "enzyme-adapter-react-16";
configure({adapter: new Adapter()});

const props = {
  testConnection: jest.fn()
};

describe("DashboardConfigDatasourceStatus", () => {
  let component;
  beforeEach(() => {
    component = shallow(<DashboardConfigDatasourceStatus {...props} />);
  });

  it("renders without crashing", () => {
    expect(component.exists()).toBe(true);
  });

  it("have one label and  ond button ", () => {
    expect(component.find(Label)).toHaveLength(1);
    expect(component.find(Button)).toHaveLength(1);
  });

  it("clicking on button test component call testConnection", () => {
    const event = component.find(Button);
    event.simulate("click");
    expect(props.testConnection).toHaveBeenCalled();
  });
});
