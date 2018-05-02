import { config } from '@entando/apimanager';

import uiComponent from 'ui/server-config/common/SubRouterContainer';
import reducer from 'state/main/reducer';

import enLocale from 'locales/en';
import itLocale from 'locales/it';

import BpmChannelForm from 'ui/widgets/BpmChannelForm';

import { name as id } from '../package.json';


const plugin = {
  id,
  menuItemLabelId: 'plugin.title',
  uiComponent,
  reducer,
  locales: [
    enLocale,
    itLocale,
  ],
  // workaround to use apimanager (the plugins compilation has to be pulled out from webpack)
  apiManagerConfig: config,
  widgetForms: {
    'bpm-case-comments': BpmChannelForm,
    'bpm-case-actions': BpmChannelForm,
    'bpm-case-chart': BpmChannelForm,
    'bpm-case-details': BpmChannelForm,
    'bpm-case-file': BpmChannelForm,
    'bpm-case-roles': BpmChannelForm,
    'bpm-process-diagram': BpmChannelForm,
  },
};

export default plugin;
