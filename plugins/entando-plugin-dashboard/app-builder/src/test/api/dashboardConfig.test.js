import "test/enzyme-init";
import {
  getServerType,
  getServerConfig,
  postServerConfig,
  putServerConfig,
  deleteServerConfig,
  getDatasources,
  putDatasourceColumn,
  getDatasourceColumns
} from "api/dashboardConfig";
import {makeRequest, METHODS} from "@entando/apimanager";

jest.unmock("api/dashboardConfig");
jest.mock("@entando/apimanager");

jest.mock("@entando/apimanager", () => ({
  makeRequest: jest.fn(obj => new Promise(resolve => resolve(obj))),
  METHODS: require.requireActual("@entando/apimanager").METHODS
}));

const CONFIG = {
  active: true,
  debug: false,
  id: "1",
  serverDescription: "KAA IoT Server Devices CRS4",
  username: "admin",
  password: "adminadmin",
  serverURI: "http://kaa.entando.iot.com:3303",
  token: "token-code",
  timeoutConnection: 300,
  datasources: [
    {
      datasource: "Temperature",
      datasourceURI: "/api/temperature"
    }
  ]
};

describe("api/dashboardConfig", () => {
  beforeEach(() => {
    jest.clearAllMocks();
  });

  describe("getServerType", () => {
    it("returns a promise", () => {
      expect(getServerType()).toBeInstanceOf(Promise);
    });

    it("if successful, returns a mock ok response", () => {
      getServerType();
      expect(makeRequest).toHaveBeenCalledWith(
        expect.objectContaining({
          uri: "/api/plugins/dashboard/dashboardConfigs/servertypes",
          method: METHODS.GET,
          useAuthentication: true
        })
      );
    });
  });

  describe("getServerConfig", () => {
    it("returns a promise", () => {
      expect(getServerConfig()).toBeInstanceOf(Promise);
    });

    it("if successful, returns a mock ok response", () => {
      getServerConfig();
      expect(makeRequest).toHaveBeenCalledWith(
        expect.objectContaining({
          uri: "/api/plugins/dashboard/dashboardConfigs",
          method: METHODS.GET,
          useAuthentication: true
        })
      );
    });

    it("makes the request with additional params", () => {
      getServerConfig({id: 1});
      expect(makeRequest).toHaveBeenCalledWith(
        expect.objectContaining({
          uri: `/api/plugins/dashboard/dashboardConfigs/1`,
          method: METHODS.GET,
          useAuthentication: true
        })
      );
    });
  });

  describe("postServerConfig", () => {
    it("returns a promise", () => {
      expect(postServerConfig()).toBeInstanceOf(Promise);
    });

    it("if successful, returns a mock ok response", () => {
      postServerConfig(CONFIG);
      expect(makeRequest).toHaveBeenCalledWith(
        expect.objectContaining({
          uri: "/api/plugins/dashboard/dashboardConfigs",
          method: METHODS.POST,
          useAuthentication: true,
          body: CONFIG
        })
      );
    });
  });

  describe("putServerConfig", () => {
    it("returns a promise", () => {
      expect(putServerConfig(CONFIG)).toBeInstanceOf(Promise);
    });

    it("if successful, returns a mock ok response", () => {
      putServerConfig(CONFIG);
      expect(makeRequest).toHaveBeenCalledWith(
        expect.objectContaining({
          uri: "/api/plugins/dashboard/dashboardConfigs/1",
          method: METHODS.PUT,
          useAuthentication: true,
          body: CONFIG
        })
      );
    });
  });

  describe("deleteServerConfig", () => {
    it("returns a promise", () => {
      expect(deleteServerConfig(1)).toBeInstanceOf(Promise);
    });

    it("if successful, returns a mock ok response", () => {
      deleteServerConfig(1);
      expect(makeRequest).toHaveBeenCalledWith(
        expect.objectContaining({
          uri: "/api/plugins/dashboard/dashboardConfigs/1",
          method: METHODS.DELETE,
          useAuthentication: true
        })
      );
    });
  });

  describe("getDatasources", () => {
    it("returns a promise", () => {
      expect(getDatasources(1)).toBeInstanceOf(Promise);
    });

    it("if successful, returns a mock ok response", () => {
      getDatasources(1);
      expect(makeRequest).toHaveBeenCalledWith(
        expect.objectContaining({
          uri: "/api/plugins/dashboard/dashboardConfigs/1/datasources",
          method: METHODS.GET,
          useAuthentication: true
        })
      );
    });
  });

  describe("putDatasourceColumn", () => {
    const columns = [{key: "temperature", value: "temperature"}];
    it("returns a promise", () => {
      expect(putDatasourceColumn(1, 2, columns)).toBeInstanceOf(Promise);
    });

    it("if successful, returns a mock ok response", () => {
      putDatasourceColumn(1, 2, columns);
      expect(makeRequest).toHaveBeenCalledWith(
        expect.objectContaining({
          uri:
            "/api/plugins/dashboard/dashboardConfigs/server/1/datasource/2/columns",
          method: METHODS.PUT,
          useAuthentication: true,
          body: columns
        })
      );
    });
  });

  describe("getDatasourceColumns", () => {
    it("returns a promise", () => {
      expect(getDatasourceColumns(2, "temperature")).toBeInstanceOf(Promise);
    });

    it("if successful, returns a mock ok response", () => {
      getDatasourceColumns(2, "temperature");
      expect(makeRequest).toHaveBeenCalledWith(
        expect.objectContaining({
          uri:
            "/api/plugins/dashboard/dashboardConfigs/server/2/datasource/temperature/columns",
          method: METHODS.GET,
          useAuthentication: true
        }),
        {page: 1, pageSize: 0}
      );
    });
  });
});
