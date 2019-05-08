import 'test/enzyme-init';
import { shallow } from 'enzyme';

import React from 'react';
import SubRouter from 'ui/dashboard-config/common/components/SubRouter';
import ConfigPageContainer from 'ui/dashboard-config/list/containers/DashboardConfigPageContainer';
import DashboardConfigAddPage from 'ui/dashboard-config/add/components/DashboardConfigAddPage';
import DashboardConfigEditPage from 'ui/dashboard-config/add/components/DashboardConfigEditPage';

describe('SubRouter', () => {
  let component;

  it('renders default component  ConfigPageContainer', () => {
    component = shallow(<SubRouter pluginPage="" />);
    expect(component.find(ConfigPageContainer)).toHaveLength(1);
  });

  it("props pluginPage equals to 'add' render DashboardConfigAddPage ", () => {
    component = shallow(<SubRouter pluginPage="add" />);
    expect(component.find(DashboardConfigAddPage)).toHaveLength(1);
  });

  it("props pluginPage equals to 'edit' render DashboardConfigEditPage ", () => {
    component = shallow(<SubRouter pluginPage="edit" />);
    expect(component.find(DashboardConfigEditPage)).toHaveLength(1);
  });
});
