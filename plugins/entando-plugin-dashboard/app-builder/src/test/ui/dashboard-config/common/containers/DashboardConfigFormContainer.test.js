import {
  mapStateToProps,
  mapDispatchToProps
} from "ui/dashboard-config/common/containers/DashboardConfigFormContainer";

import {
  fetchServerType,
  createServerConfig,
  updateServerConfig,
  setInternalRoute
} from "state/main/actions";
const dispatchMock = jest.fn();

jest.mock("state/main/actions");
jest.mock("state/main/selectors");

const DATASOURCE_VALUE = {
  datasourceCode: "xxx",
  datasource: "Temperature",
  datasourceURI: "/devices/temperature"
};

describe("DashboardConfigFormContainer", () => {
  let props;
  describe("mapStateToProps", () => {
    it("maps properties state in DashboardConfigForm", () => {
      props = mapStateToProps({});
      expect(props).toHaveProperty("datasourceValue");
      expect(props).toHaveProperty("datasourceValue.datasource");
      expect(props).toHaveProperty("datasourceValue.datasourceURI");
      expect(props).toHaveProperty("datasources");
      expect(props).toHaveProperty("serverTypeList");
    });
  });

  describe("mapDispatchToProps", () => {
    it("should map the correct function properties", () => {
      props = mapDispatchToProps(dispatchMock);
      expect(props.onWillMount).toBeDefined();
      expect(props.onSubmit).toBeDefined();
      expect(props.gotoHomePage).toBeDefined();
      expect(props.testConnection).toBeDefined();
    });

    it("dispatch function onWillMount", () => {
      props = mapDispatchToProps(dispatchMock);
      props.onWillMount();
      expect(fetchServerType).toHaveBeenCalled();
    });

    it("prop mode equals to add dispatch function createServerConfig", () => {
      props = mapDispatchToProps(dispatchMock, {mode: "add"});
      props.onSubmit(DATASOURCE_VALUE);
      expect(createServerConfig).toHaveBeenCalled();
    });

    it("dispatch function gotoHomePage", () => {
      props = mapDispatchToProps(dispatchMock);
      props.gotoHomePage();
      expect(setInternalRoute).toHaveBeenCalled();
    });

    it("prop mode equals to edit dispatch function createServerConfig", () => {
      props = mapDispatchToProps(dispatchMock, {mode: "edit"});
      props.onSubmit(DATASOURCE_VALUE);
      expect(updateServerConfig).toHaveBeenCalled();
    });
  });
});
