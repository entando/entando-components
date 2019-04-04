import React from "react";
import "test/enzyme-init";
import {shallow} from "enzyme";
import {FieldArray} from "redux-form";

import SettingsChartDonut from "ui/widgets/charts/donut-chart/components/SettingsChartDonut";

const props = {
  optionColumns: [],
  optionColumnXSelected: []
};

describe("SettingsChartDonut", () => {
  let component;
  beforeEach(() => {
    component = shallow(<SettingsChartDonut {...props} />);
  });

  it("renders without crashing", () => {
    expect(component.exists()).toBe(true);
  });

  it("have FieldArray", () => {
    expect(component.find(FieldArray)).toHaveLength(1);
  });
});
