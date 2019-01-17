import {config} from "@entando/apimanager";

import uiComponent from "ui/dashboard-config/common/containers/SubRouterContainer";
import DashboardTable from "ui/widgets/dashboard-table/components/DashboardTable";

import reducer from "state/main/reducer";

import enLocale from "locales/en";
import itLocale from "locales/it";

import {name as id} from "../package.json";

import "./sass/index.css";
import "react-bootstrap-typeahead/css/Typeahead.css";

import {setCurrentLocale} from "@entando/utils";
setCurrentLocale(enLocale);

const plugin = {
  id,
  menuItemLabelId: "menu.itemLabel",
  uiComponent,
  reducer,
  locales: [enLocale, itLocale],
  // workaround to use apimanager (the plugins compilation has to be pulled out from webpack)
  apiManagerConfig: config,
  widgetForms: {
    "dashboard-table": DashboardTable
  }
};

export default plugin;
