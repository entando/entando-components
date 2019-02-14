import React from "react";

import DashboardConfigDatasource from "ui/dashboard-config/common/components/DashboardConfigDatasource";
import {Button} from "patternfly-react";

import {DASHBOARD_LIST_DATASOURCE} from "mocks/dashboardConfigs";

import {configure, shallow} from "enzyme";
import Adapter from "enzyme-adapter-react-16";
configure({adapter: new Adapter()});

const DATASOURCE_VALUE = {
  name: "Temperature",
  uri: "/devices/temperature"
};

describe("DashboardConfigDatasourceStatus", () => {
  let component;
  beforeEach(() => {
    component = shallow(<DashboardConfigDatasource />);
  });

  it("renders without crashing", () => {
    expect(component.exists()).toBe(true);
  });

  it("if props datasourceValue is empty button add is diasbled", () => {
    expect(component.find(Button).prop("disabled")).toBeTruthy();
  });

  it("if props datasourceValue is not empty button add is not diabled", () => {
    component = shallow(
      <DashboardConfigDatasource datasourceValue={DATASOURCE_VALUE} />
    );
    expect(component.find(Button).prop("disabled")).toBeFalsy();
  });

  it("if props datasourceValue is not empty click button add  calle the function push on the field props", () => {
    const props = {
      fields: {
        push: jest.fn(),
        remove: jest.fn()
      },
      datasourceValue: DATASOURCE_VALUE
    };
    component = shallow(<DashboardConfigDatasource {...props} />);
    const event = component.find(Button);
    expect(component.find(Button).prop("disabled")).toBeFalsy();
    event.simulate("click");
    expect(props.fields.push).toHaveBeenCalledWith(DATASOURCE_VALUE);
  });

  it("if props datasources is not empty show table ", () => {
    component = shallow(
      <DashboardConfigDatasource datasources={DASHBOARD_LIST_DATASOURCE["1"]} />
    );
    expect(component.find("table")).toHaveLength(1);
    expect(component.find("thead")).toHaveLength(1);
    expect(component.find("tr th")).toHaveLength(4);
    expect(component.find("tbody")).toHaveLength(1);
  });
});
