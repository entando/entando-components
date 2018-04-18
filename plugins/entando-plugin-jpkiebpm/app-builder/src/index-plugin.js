
import uiComponent from 'ui/plugin/containers/PluginContainer';
import reducer from 'state/main/reducer';

import enLocale from 'locales/en';
import itLocale from 'locales/it';

import BpmCaseCommentsForm from 'ui/widgets/BpmCaseCommentsForm';

import { name as id } from '../package.json';


import './sass/index.css';


const plugin = {
  id,
  menuItemLabelId: 'menu.itemLabel',
  uiComponent,
  reducer,
  locales: [
    enLocale,
    itLocale,
  ],
  widgetForms: {
    'bpm-case-comments': BpmCaseCommentsForm,
  },
};

export default plugin;
