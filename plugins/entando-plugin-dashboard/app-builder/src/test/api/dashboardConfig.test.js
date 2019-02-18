import "test/enzyme-init";
import {
  getServerConfig,
  postServerConfig,
  putServerConfig,
  deleteServerConfig,
  getDatasources
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
  timeout: 300,
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
});
