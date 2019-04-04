import React from "react";
import "test/enzyme-init";
import {shallow} from "enzyme";
import {FieldArray} from "redux-form";

import SettingsChartPie from "ui/widgets/charts/pie-chart/components/SettingsChartPie";

const props = {
  optionColumns: [],
  optionColumnXSelected: []
};

describe("SettingsChartPie", () => {
  let component;
  beforeEach(() => {
    component = shallow(<SettingsChartPie {...props} />);
  });

  it("renders without crashing", () => {
    expect(component.exists()).toBe(true);
  });

  it("have FieldArray", () => {
    expect(component.find(FieldArray)).toHaveLength(1);
  });
});
