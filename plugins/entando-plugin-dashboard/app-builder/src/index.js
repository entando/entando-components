import { config } from '@entando/apimanager';
import { setCurrentLocale } from '@entando/utils';

import uiComponent from 'ui/dashboard-config/common/containers/SubRouterContainer';
import DashboardTable from 'ui/widgets/table/components/DashboardTable';
import DashboardLineChart from 'ui/widgets/charts/line-chart/components/DashboardLineChart';
import DashboardBarChart from 'ui/widgets/charts/bar-chart/components/DashboardBarChart';
import DashboardDonutChart from 'ui/widgets/charts/donut-chart/components/DashboardDonutChart';
import DashboardGaugeChart from 'ui/widgets/charts/gauge-chart/components/DashboardGaugeChart';
import DashboardPieChart from 'ui/widgets/charts/pie-chart/components/DashboardPieChart';
import DashboardMapChart from 'ui/widgets/geolocalization/components/DashboardMap';

import reducer from 'state/main/reducer';

import enLocale from 'locales/en';
import itLocale from 'locales/it';

import 'react-bootstrap-typeahead/css/Typeahead.css';

import { name as id } from '../package.json';

import './sass/index.css';


setCurrentLocale(enLocale);

const plugin = {
  id,
  menuItemLabelId: 'menu.itemLabel',
  uiComponent,
  reducer,
  locales: [enLocale, itLocale],
  // workaround to use apimanager (the plugins compilation has to be pulled out from webpack)
  apiManagerConfig: config,
  widgetForms: {
    'dashboard-table': DashboardTable,
    'dashboard-line-chart': DashboardLineChart,
    'dashboard-bar-chart': DashboardBarChart,
    'dashboard-donut-chart': DashboardDonutChart,
    'dashboard-gauge-chart': DashboardGaugeChart,
    'dashboard-pie-chart': DashboardPieChart,
    'dashboard-map': DashboardMapChart,
  },
};

export default plugin;
