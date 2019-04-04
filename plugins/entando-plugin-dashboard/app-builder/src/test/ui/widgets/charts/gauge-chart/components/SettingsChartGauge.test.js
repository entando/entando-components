import React from "react";
import "test/enzyme-init";
import {shallow} from "enzyme";
import {FieldArray} from "redux-form";

import SettingsChartGauge from "ui/widgets/charts/gauge-chart/components/SettingsChartGauge";

const props = {
  optionColumns: [],
  optionColumnXSelected: []
};

describe("SettingsChartGauge", () => {
  let component;
  beforeEach(() => {
    component = shallow(<SettingsChartGauge {...props} />);
  });

  it("renders without crashing", () => {
    expect(component.exists()).toBe(true);
  });

  it("have FieldArray", () => {
    expect(component.find(FieldArray)).toHaveLength(1);
  });
});
