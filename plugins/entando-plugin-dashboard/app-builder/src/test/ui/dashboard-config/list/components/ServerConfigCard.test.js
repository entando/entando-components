import React from "react";

import {configure, shallow} from "enzyme";
import Adapter from "enzyme-adapter-react-16";
import {
  Card,
  CardTitle,
  CardBody,
  DropdownKebab,
  MenuItem
} from "patternfly-react";

import ServerConfigCard from "ui/dashboard-config/list/components/ServerConfigCard";

configure({adapter: new Adapter()});

const props = {
  configItem: {},
  onClickRemove: jest.fn(),
  onClickTest: jest.fn(),
  onClickEdit: jest.fn()
};

describe("ServerConfigCard", () => {
  let component;
  beforeEach(() => {
    component = shallow(<ServerConfigCard {...props} />);
  });

  it("renders without crashing", () => {
    expect(component.exists()).toBe(true);
  });

  it("contains Card", () => {
    expect(component.find(Card)).toHaveLength(1);
  });

  it("contains CardTitle", () => {
    expect(component.find(CardTitle)).toHaveLength(1);
  });

  it("contains CardBody", () => {
    expect(component.find(CardBody)).toHaveLength(1);
  });

  it("contains MenuItem", () => {
    expect(component.find(MenuItem)).toHaveLength(3);
  });

  it("contains DropdownKebab", () => {
    expect(component.find(DropdownKebab)).toHaveLength(1);
  });

  it("clicking on test MenuItem component calls onClickTest", () => {
    const event = component.find(".ServerConfigCard__menu-item-test");
    event.simulate("click");
    expect(props.onClickTest).toHaveBeenCalled();
  });

  it("clicking on test MenuItem component calls onClickEdit", () => {
    const event = component.find(".ServerConfigCard__menu-item-edit");
    event.simulate("click");
    expect(props.onClickEdit).toHaveBeenCalled();
  });

  it("clicking on test MenuItem component calls onClickRemove", () => {
    const event = component.find(".ServerConfigCard__menu-item-remove");
    event.simulate("click");
    expect(props.onClickRemove).toHaveBeenCalled();
  });
});
