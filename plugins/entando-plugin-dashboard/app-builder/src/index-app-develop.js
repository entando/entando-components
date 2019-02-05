import React from "react";
import ReactDOM from "react-dom";

import {Grid, Row, Col} from "patternfly-react";

import registerServiceWorker from "registerServiceWorker";
import {setCurrentLocale} from "@entando/utils";

// state manager (Redux)
import {Provider} from "react-redux";
import store from "state/store";

// IntlProvider
import {IntlProvider} from "react-intl";

// use en locale by default
import enLocale from "locales/en";
import plugin from "./index";

import "./sass/index.css";
import "patternfly/dist/css/patternfly.min.css";
import "patternfly/dist/css/patternfly-additions.min.css";
import "react-bootstrap-typeahead/css/Typeahead.css";
import DashboardMap from "ui/widgets/geolocalization/components/DashboardMap";
import DashboardPieChart from "ui/widgets/charts/pie-chart/components/DashboardPieChart";

const mappedMessages = Object.keys(enLocale.messages).reduce((acc, key) => {
  acc[`plugin.${plugin.id}.${key}`] = enLocale.messages[key];
  return acc;
}, {});

setCurrentLocale(enLocale);

// exporting for tests
export default ReactDOM.render(
  <Provider store={store}>
    <IntlProvider locale={enLocale.locale} messages={mappedMessages}>
      <Grid fluid>
        <Row>
          <Col xs={12}>
            <DashboardMap />
          </Col>
        </Row>
        <Row>
          <Col xs={12}>
            <DashboardPieChart />
          </Col>
        </Row>
      </Grid>
    </IntlProvider>
  </Provider>,
  document.getElementById("root")
);
registerServiceWorker();
