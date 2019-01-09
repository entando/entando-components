import React from "react";

import {configure, shallow} from "enzyme";
import Adapter from "enzyme-adapter-react-16";

import DashboardConfigPage from "ui/dashboard-config/list/Components/DashboardConfigPage";

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
});
