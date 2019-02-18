import "test/enzyme-init";
import {shallow} from "enzyme";

import React from "react";
import {Field, FieldArray} from "redux-form";
import {DashboardConfigFormBody} from "ui/dashboard-config/common/components/DashboardConfigForm";

const props = {
  handleSubmit: jest.fn(),
  testConnection: jest.fn(),
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

  it("contain one form ", () => {
    expect(component.find("form")).toHaveLength(1);
  });

  it("on form submit calls handleSubmit", () => {
    const preventDefault = jest.fn();
    component.find("form").simulate("submit", {preventDefault});
    expect(props.handleSubmit).toHaveBeenCalled();
  });

  it("have FieldArray", () => {
    expect(component.find(FieldArray)).toHaveLength(1);
  });
  it("have Fields", () => {
    expect(component.find(Field)).toHaveLength(10);
  });
});
