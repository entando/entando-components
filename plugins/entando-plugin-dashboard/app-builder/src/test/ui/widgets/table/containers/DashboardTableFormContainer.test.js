import {
  mapStateToProps,
  mapDispatchToProps
} from "ui/widgets/table/containers/DashboardTableFormContainer";

import {
  fetchServerConfigList,
  getWidgetConfig,
  gotoConfigurationPage
} from "state/main/actions";

const FORM_DATA = {
  allColumns: "true",
  serverName: "14",
  datasource: "Temperature",
  options: {
    filtrable: "true",
    downlodable: "true"
  },
  columns: {temperature: "temperature", timestamp: "timestamp"},
  title: {
    en: "titolo"
  }
};

jest.mock("state/main/actions");

// jest.mock("state/main/actions", () => ({
//   fetchServerConfigList: jest.fn(),
//   getWidgetConfig: jest.fn()
// }));

fetchServerConfigList.mockImplementation(() => Promise.resolve({}));

const ownProps = {
  onSubmit: jest.fn()
};

describe("DashboardTableFormContainer", () => {
  let container;

  describe("mapStateToProps", () => {
    it("maps properties state in DashboardTableForm", () => {
      container = mapStateToProps({});
      expect(container).toHaveProperty("datasource");
      expect(container).toHaveProperty("initialValues");
      expect(container).toHaveProperty("initialValues.allColumns");
      expect(container).toHaveProperty("initialValues.options.downlodable");
      expect(container).toHaveProperty("initialValues.options.filtrable");
    });
  });

  describe("mapDispatchToProps", () => {
    const dispatchMock = jest.fn(args => args);
    beforeEach(() => {
      jest.clearAllMocks();
      container = mapDispatchToProps(dispatchMock, ownProps);
    });

    it("should map the correct function properties", () => {
      expect(container.onWillMount).toBeDefined();
      expect(container.onSubmit).toBeDefined();
      expect(container.onCancel).toBeDefined();
    });

    it("should call onWillMount and dispatch action fetchServerConfigList and getTableWidgetConfig", done => {
      container.onWillMount();
      expect(fetchServerConfigList).toHaveBeenCalled();
      fetchServerConfigList().then(() => {
        expect(getWidgetConfig).toHaveBeenCalledWith("form-dashboard-table");
      });
      done();
    });

    it("should call onSubmit dispatch ownProps.onSubmit", () => {
      container.onSubmit(FORM_DATA);
      expect(ownProps.onSubmit).toHaveBeenCalled();
    });

    it("should call onCancel dispatch gotoConfigurationPage", () => {
      container.onCancel();
      expect(gotoConfigurationPage).toHaveBeenCalled();
    });
  });
});
