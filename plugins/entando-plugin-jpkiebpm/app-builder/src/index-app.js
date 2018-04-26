import React from 'react';
import ReactDOM from 'react-dom';

import PluginContainer from 'ui/plugin/containers/PluginContainer';
import BpmCaseCommentsForm from 'ui/widgets/BpmCaseCommentsForm';
import ConfigPageContainer from 'ui/config-page/ConfigPageContainer';
import registerServiceWorker from 'registerServiceWorker';

// state manager (Redux)
import { Provider } from 'react-redux';
import store from 'state/store';

// IntlProvider
import { IntlProvider } from 'react-intl';

// use en locale by default
import enLocale from 'locales/en';
import plugin from 'index';

// wrapper for forms
import { reduxForm } from 'redux-form';

import 'patternfly/dist/css/patternfly.min.css';
import 'patternfly/dist/css/patternfly-additions.min.css';
import '../dist/index.css'; // if not present, run "npm run build-css"


const mappedMessages = Object.keys(enLocale.messages)
  .reduce((acc, key) => {
    acc[`plugin.${plugin.id}.${key}`] = enLocale.messages[key];
    return acc;
  }, {});


// wrapped forms
const WrappedBpmCaseCommentsForm = reduxForm('BpmCaseCommentsForm')(BpmCaseCommentsForm);

// exporting for tests
export default ReactDOM.render(
  <Provider store={store}>
    <IntlProvider locale={enLocale.locale} messages={mappedMessages}>
      <div>
        <PluginContainer />
        <WrappedBpmCaseCommentsForm />
        <ConfigPageContainer />
      </div>
    </IntlProvider>
  </Provider>,
  document.getElementById('root'),
);
registerServiceWorker();
