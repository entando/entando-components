import React from 'react';
import ReactDOM from 'react-dom';

import PluginContainer from 'ui/plugin/containers/PluginContainer';
import BpmCaseCommentsForm from 'ui/widgets/BpmCaseCommentsForm';
import BpmCaseDetailsForm from 'ui/widgets/BpmCaseDetailsForm';
// import BpmCaseFileForm from 'ui/widgets/BpmCaseFileForm';
import BpmCaseInstanceSelectorForm from 'ui/widgets/BpmCaseInstanceSelectorForm';
// import BpmChartForm from 'ui/widgets/BpmChartForm';
// import BpmProgressStatusForm from 'ui/widgets/BpmProgressStatusForm';
// import BpmRolesForm from 'ui/widgets/BpmRolesForm';
import registerServiceWorker from 'registerServiceWorker';

// state manager (Redux)
import { Provider } from 'react-redux';
import store from 'state/store';

// IntlProvider
import { IntlProvider } from 'react-intl';

// use en locale by default
import enLocale from 'locales/en';
import plugin from 'index-plugin';

// wrapper for forms
import { reduxForm } from 'redux-form';

import 'patternfly/dist/css/patternfly.min.css';
import 'patternfly/dist/css/patternfly-additions.min.css';
import './sass/index.css';


const mappedMessages = Object.keys(enLocale.messages)
  .reduce((acc, key) => {
    acc[`plugin.${plugin.id}.${key}`] = enLocale.messages[key];
    return acc;
  }, {});


// wrapped forms
const WrappedBpmCaseCommentsForm = reduxForm('BpmCaseCommentsForm')(BpmCaseCommentsForm);
const WrappedBpmCaseDetailsForm = reduxForm('BpmCaseDetailsForm')(BpmCaseDetailsForm);
const WrappedBpmCaseInstanceSelectorForm = reduxForm('BpmCaseInstanceSelectorForm')(BpmCaseInstanceSelectorForm);

// exporting for tests
export default ReactDOM.render(
  <Provider store={store}>
    <IntlProvider locale={enLocale.locale} messages={mappedMessages}>
      <div>
        <PluginContainer />
        <WrappedBpmCaseCommentsForm onSubmit={form => console.log(`test${form}`)} />
        <WrappedBpmCaseDetailsForm />
        <WrappedBpmCaseInstanceSelectorForm />
      </div>
    </IntlProvider>
  </Provider>,
  document.getElementById('root'),
);
registerServiceWorker();
