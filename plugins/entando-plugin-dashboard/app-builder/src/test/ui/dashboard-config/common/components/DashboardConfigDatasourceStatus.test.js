import 'test/enzyme-init';
import { shallow } from 'enzyme';

import React from 'react';
import DashboardConfigDatasourceStatus from 'ui/dashboard-config/common/components/DashboardConfigDatasourceStatus';
import { Label } from 'patternfly-react';

const props = {
  testConnection: jest.fn(),
};

describe('DashboardConfigDatasourceStatus', () => {
  let component;
  beforeEach(() => {
    component = shallow(<DashboardConfigDatasourceStatus {...props} />);
  });

  it('renders without crashing', () => {
    expect(component.exists()).toBe(true);
  });

  it('have one label and  ond button ', () => {
    expect(component.find(Label)).toHaveLength(1);
  });
});
