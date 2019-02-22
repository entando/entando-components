import React from "react";
import "test/enzyme-init";
import {shallow} from "enzyme";

import {Field} from "redux-form";

import {DashboardTableFormBody} from "ui/widgets/table/components/DashboardTableForm";
import DashboardWidgetTitleContainer from "ui/widgets/common/form/containers/DashboardWidgetTitleContainer";
import ServerNameFormContainer from "ui/widgets/common/form/containers/ServerNameFormContainer";
import DatasourceFormContainer from "ui/widgets/common/form/containers/DatasourceFormContainer";

const props = {
  handleSubmit: jest.fn(),
  onWillMount: jest.fn(),
  invalid: false,
  submitting: false
};

describe("DashboardTableForm", () => {
  let component;
  beforeEach(() => {
    component = shallow(<DashboardTableFormBody {...props} />);
  });

  it("renders without crashing", () => {
    expect(component.exists()).toBe(true);
  });

  it("contain one form ", () => {
    expect(component.find("form")).toHaveLength(1);
  });

  it("contain three Field component ", () => {
    expect(component.find(Field)).toHaveLength(3);
  });

  it("contain one DashboardWidgetTitleContainer component ", () => {
    expect(component.find(DashboardWidgetTitleContainer)).toHaveLength(1);
  });

  it("contain one ServerNameFormContainer component ", () => {
    expect(component.find(ServerNameFormContainer)).toHaveLength(1);
  });

  it("contain one ServerNameFormContainer component ", () => {
    expect(component.find(DatasourceFormContainer)).toHaveLength(1);
  });

  it("will call onWillMount on componentWillMount", () => {
    expect(props.onWillMount).toHaveBeenCalled();
  });

  it("disables submit button while submitting", () => {
    props.submitting = true;
    component = shallow(<DashboardTableFormBody {...props} />);
    const submitButton = component.find("[type='submit']");
    expect(submitButton.prop("disabled")).toBe(true);
  });

  it("disables submit button if form is invalid", () => {
    props.invalid = true;
    component = shallow(<DashboardTableFormBody {...props} />);
    const submitButton = component.find("[type='submit']");
    expect(submitButton.prop("disabled")).toBe(true);
  });

  it("on form submit calls handleSubmit", () => {
    const preventDefault = jest.fn();
    component.find("form").simulate("submit", {preventDefault});
    expect(props.handleSubmit).toHaveBeenCalled();
  });

  it("change toogle Columns Information", () => {
    const event = component.find(".DashboardTableForm__switch-all-columns");
    event.simulate("change");
    expect(component.state("toggleAllColums")).toBe(false);
  });

  it("disables submit button if form is invalid", () => {
    props.datasource = "temp";
    component = shallow(<DashboardTableFormBody {...props} />);
    const toggle = component.find(".DashboardTableForm__switch-all-columns");
    expect(toggle.prop("disabled")).toBe(false);
  });
});
