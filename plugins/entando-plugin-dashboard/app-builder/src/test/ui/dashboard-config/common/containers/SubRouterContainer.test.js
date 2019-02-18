import {mapStateToProps} from "ui/dashboard-config/common/containers/SubRouterContainer";

import {getPluginPage, getInternalRoute} from "state/main/selectors";

jest.mock("state/main/selectors");

const INITIAL_STATE = {};

describe("SubRouterContainer", () => {
  it("maps pluginPage property state in SubRouter", () => {
    const props = mapStateToProps(INITIAL_STATE);
    expect(props).toHaveProperty("pluginPage");
  });

  it("check return the value of internalRoute state", () => {
    getPluginPage.mockReturnValueOnce("");
    getInternalRoute.mockReturnValueOnce("add");
    const props = mapStateToProps(INITIAL_STATE);
    expect(props).toHaveProperty("pluginPage", "add");
  });
});
