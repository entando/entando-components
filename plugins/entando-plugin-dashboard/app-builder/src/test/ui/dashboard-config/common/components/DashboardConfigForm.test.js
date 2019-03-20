import "test/enzyme-init";
import {shallow} from "enzyme";

import React from "react";
import {Field, FieldArray} from "redux-form";
import {DashboardConfigFormBody} from "ui/dashboard-config/common/components/DashboardConfigForm";

const props = {
  onWillMount: jest.fn(),
  handleSubmit: jest.fn(),
  gotoHomePage: jest.fn(),
  testConnection: jest.fn(),
  serverTypeList: [],
  datasources: [],
  invalid: false,
  submitting: false
};

describe("DashboardConfigForm", () => {
  let component;
  beforeEach(() => {
    component = shallow(<DashboardConfigFormBody {...props} />);
  });

  it("renders without crashing", () => {
    expect(component.exists()).toBe(true);
  });

  it("will call onWillMount on componentWillMount", () => {
    expect(props.onWillMount).toHaveBeenCalled();
  });

  it("contain one form ", () => {
    expect(component.find("form")).toHaveLength(1);
  });

  it("on form submit calls handleSubmit", () => {
    const preventDefault = jest.fn();
    component.find("form").simulate("submit", {preventDefault});
    expect(props.handleSubmit).toHaveBeenCalled();
  });

  it("click cancel button calls gotoHomePage", () => {
    component.find(".DashboardConfig__btn-cancel").simulate("click");
    expect(props.gotoHomePage).toHaveBeenCalled();
  });

  it("have FieldArray", () => {
    expect(component.find(FieldArray)).toHaveLength(1);
  });

  it("have Fields", () => {
    expect(component.find(Field)).toHaveLength(12);
  });
});
