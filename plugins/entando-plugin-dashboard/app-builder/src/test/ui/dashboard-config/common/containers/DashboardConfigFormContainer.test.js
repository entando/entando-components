import {
  mapStateToProps,
  mapDispatchToProps
} from "ui/dashboard-config/common/containers/DashboardConfigFormContainer";

import {createServerConfig, updateServerConfig} from "state/main/actions";
const dispatchMock = jest.fn();

jest.mock("state/main/actions");

describe("DashboardConfigFormContainer", () => {
  let props;
  describe("mapStateToProps", () => {
    it("maps properties state in DashboardConfigForm", () => {
      props = mapStateToProps({});
      expect(props).toHaveProperty("datasourceValue");
      expect(props).toHaveProperty("datasources");
    });
  });

  describe("mapDispatchToProps", () => {
    it("should map the correct function properties", () => {
      props = mapDispatchToProps(dispatchMock);
      expect(props.onSubmit).toBeDefined();
      expect(props.testConnection).toBeDefined();
    });

    it("prop mode equals to add dispatch function createServerConfig  ", () => {
      props = mapDispatchToProps(dispatchMock, {mode: "add"});
      props.onSubmit();
      expect(createServerConfig).toHaveBeenCalled();
    });

    it("prop mode equals to edit dispatch function createServerConfig  ", () => {
      props = mapDispatchToProps(dispatchMock, {mode: "edit"});
      props.onSubmit();
      expect(updateServerConfig).toHaveBeenCalled();
    });
  });
});
