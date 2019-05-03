import React from "react";
import ReactDOM from "react-dom";

import {createStore, compose, applyMiddleware, combineReducers} from "redux";
import {reducer as formReducer} from "redux-form";

import thunk from "redux-thunk";
import rootReducer from "state/main/reducer";

import {config, api, setApi} from "@entando/apimanager";

import {name} from "../package.json";

import {Grid, Row, Col} from "patternfly-react";

import registerServiceWorker from "registerServiceWorker";
import {setCurrentLocale} from "@entando/utils";

// state manager (Redux)
import {Provider} from "react-redux";

// IntlProvider
import {IntlProvider} from "react-intl";

// use en locale by default
import enLocale from "locales/en";
import plugin from "./index";

import "./sass/index.css";
import "patternfly/dist/css/patternfly.min.css";
import "patternfly/dist/css/patternfly-additions.min.css";
import "react-bootstrap-typeahead/css/Typeahead.css";

import DashboardTable from "ui/widgets/table/components/DashboardTable";
import DashboardLineChart from "ui/widgets/charts/line-chart/components/DashboardLineChart";
import DashboardBarChart from "ui/widgets/charts/bar-chart/components/DashboardBarChart";
import DashboardDonutChart from "ui/widgets/charts/donut-chart/components/DashboardDonutChart";
import DashboardGaugeChart from "ui/widgets/charts/gauge-chart/components/DashboardGaugeChart";
import DashboardPieChart from "ui/widgets/charts/pie-chart/components/DashboardPieChart";
import DashboardMap from "ui/widgets/geolocalization/components/DashboardMap";

import DashboardConfigAddPage from "ui/dashboard-config/add/components/DashboardConfigAddPage";
import DashboardConfigPageContainer from "ui/dashboard-config/list/containers/DashboardConfigPageContainer";

const mappedMessages = Object.keys(enLocale.messages).reduce((acc, key) => {
  acc[`plugin.${plugin.id}.${key}`] = enLocale.messages[key];
  return acc;
}, {});

setCurrentLocale(enLocale);

/*Dichiarazione dello store. E' stato inserito qui per forzare il currentUser */

const currentUser = combineReducers({
  username: () => "admin",
  token: () => "23ee9aa496cbadca67b0a77a4eb00c01"
});

const wrappedReducer = combineReducers({
  [name]: rootReducer
});

const pluginsReducer = combineReducers({
  plugins: wrappedReducer,
  currentUser,
  api,
  form: formReducer
});

const store = createStore(
  pluginsReducer,
  undefined, // preloaded state
  compose(
    applyMiddleware(thunk),
    // eslint-disable-next-line
    window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__()
  )
);
config(store);

store.dispatch(setApi({
  domain: '//localhost:8080/entando-iot',
  useMocks: false,
}));

// exporting for tests
export default ReactDOM.render(
  <Provider store={store}>
    <IntlProvider locale={enLocale.locale} messages={mappedMessages}>
      <Grid fluid>
        <Row>
          <Col xs={12}>
            <DashboardTable />
            <DashboardDonutChart />
            <DashboardLineChart />
            <DashboardConfigAddPage />
            <DashboardPieChart />
            <DashboardDonutChart />
            <DashboardBarChart />
            <DashboardGaugeChart />
            <DashboardPieChart />
            <DashboardMap />
            <DashboardConfigPageContainer />
          </Col>
        </Row>
      </Grid>
    </IntlProvider>
  </Provider>,
  document.getElementById("entando-dashboard")
);
registerServiceWorker();
