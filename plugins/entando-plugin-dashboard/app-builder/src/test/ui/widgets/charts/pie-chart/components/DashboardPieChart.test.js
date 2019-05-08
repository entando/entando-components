import React from 'react';
import 'test/enzyme-init';
import { shallow } from 'enzyme';

import DashboardWidgetConfigPage from 'ui/widgets/common/components/DashboardWidgetConfigPage';

import DashboardPieChart from 'ui/widgets/charts/pie-chart/components/DashboardPieChart';
import DashboardPieChartFormContainer from 'ui/widgets/charts/pie-chart/containers/DashboardPieChartFormContainer';

const onSubmit = jest.fn();

describe('DashboardGaugeChart', () => {
  let component;
  beforeEach(() => {
    component = shallow(<DashboardPieChart onSubmit={onSubmit} />);
  });

  it('renders without crashing', () => {
    expect(component.exists()).toBe(true);
    expect(component.find(DashboardWidgetConfigPage)).toHaveLength(1);
    expect(component.find(DashboardPieChartFormContainer)).toHaveLength(1);
  });
});
