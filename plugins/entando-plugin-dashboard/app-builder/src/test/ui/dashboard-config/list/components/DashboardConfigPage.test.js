import React from "react";

import {configure, shallow} from "enzyme";
import Adapter from "enzyme-adapter-react-16";

import {DASHBOARD_CONFIG_LIST} from "mocks/dashboardConfigs";

import DashboardConfigPage from "ui/dashboard-config/list/components/DashboardConfigPage";
import ServerConfigCard from "ui/dashboard-config/list/components/ServerConfigCard";

configure({adapter: new Adapter()});

const props = {
  serverList: [],
  onWillMount: jest.fn(),
  removeConfigItem: jest.fn(),
  testConfigItem: jest.fn(),
  testAllConfigItems: jest.fn(),
  connectionOutcomes: {},
  gotoPluginPage: jest.fn(),
  editConfigItem: jest.fn()
};

describe("DashboardConfigPage", () => {
  let component;
  beforeEach(() => {
    component = shallow(<DashboardConfigPage {...props} />);
  });

  it("renders without crashing", () => {
    expect(component.exists()).toBe(true);
  });

  it("will call onWillMount on componentWillMount", () => {
    expect(props.onWillMount).toHaveBeenCalled();
  });
  it("simulate click add Server", () => {
    component.find(".DashboardConfigPage__btn-add").simulate("click");
    expect(props.gotoPluginPage).toHaveBeenCalledWith("add");
  });

  it("render ServerConfigCard", () => {
    props.serverList = DASHBOARD_CONFIG_LIST;
    component = shallow(<DashboardConfigPage {...props} />);
    expect(component.find(ServerConfigCard)).toHaveLength(2);
  });
});
