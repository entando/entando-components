import React from 'react';
import 'test/enzyme-init';
import { shallow } from 'enzyme';

import DashboardWidgetConfigPage from 'ui/widgets/common/components/DashboardWidgetConfigPage';

import DashboardGaugeChart from 'ui/widgets/charts/gauge-chart/components/DashboardGaugeChart';
import DashboardGaugeChartFormContainer from 'ui/widgets/charts/gauge-chart/containers/DashboardGaugeChartFormContainer';

const onSubmit = jest.fn();

describe('DashboardGaugeChart', () => {
  let component;
  beforeEach(() => {
    component = shallow(<DashboardGaugeChart onSubmit={onSubmit} />);
  });

  it('renders without crashing', () => {
    expect(component.exists()).toBe(true);
    expect(component.find(DashboardWidgetConfigPage)).toHaveLength(1);
    expect(component.find(DashboardGaugeChartFormContainer)).toHaveLength(1);
  });
});
