import { config } from '@entando/apimanager';

import uiComponent from 'ui/server-config/common/SubRouterContainer';
import TableListDevices from 'ui/widgets/table-list-devices/Components/TableListDevices';

import reducer from 'state/main/reducer';

import enLocale from 'locales/en';
import itLocale from 'locales/it';

import { name as id } from '../package.json';


import './sass/index.css';

import { setCurrentLocale } from '@entando/utils';
setCurrentLocale(enLocale);


const plugin = {
  id,
  menuItemLabelId: 'menu.itemLabel',
  uiComponent,
  reducer,
  locales: [
    enLocale,
    itLocale,
  ],
  // workaround to use apimanager (the plugins compilation has to be pulled out from webpack)
  apiManagerConfig: config,
  widgetForms: {
    'iot-table-list-devices': TableListDevices,
  },
};

export default plugin;
