import React from 'react';
import 'test/enzyme-init';
import { shallow } from 'enzyme';

import DashboardWidgetConfigPage from 'ui/widgets/common/components/DashboardWidgetConfigPage';

import DashboardBarChart from 'ui/widgets/charts/bar-chart/components/DashboardBarChart';
import DashboardBarChartFormContainer from 'ui/widgets/charts/bar-chart/containers/DashboardBarChartFormContainer';

const onSubmit = jest.fn();

describe('DashboardBarChart', () => {
  let component;
  beforeEach(() => {
    component = shallow(<DashboardBarChart onSubmit={onSubmit} />);
  });

  it('renders without crashing', () => {
    expect(component.exists()).toBe(true);
    expect(component.find(DashboardWidgetConfigPage)).toHaveLength(1);
    expect(component.find(DashboardBarChartFormContainer)).toHaveLength(1);
  });
});
