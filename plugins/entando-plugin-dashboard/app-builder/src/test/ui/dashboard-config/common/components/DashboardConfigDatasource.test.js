import 'test/enzyme-init';
import { shallow } from 'enzyme';

import React from 'react';
import DashboardConfigDatasource from 'ui/dashboard-config/common/components/DashboardConfigDatasource';
import { Button } from 'patternfly-react';
import { DASHBOARD_LIST_DATASOURCE } from 'mocks/dashboardConfigs';

const PROPS = {
  datasourceCode: '',
  testConnection: jest.fn(),
  previewDatasource: jest.fn(),
};

const DATASOURCE_VALUE = {
  datasourceCode: 'xxx',
  datasource: 'Temperature',
  datasourceURI: '/devices/temperature',

};

describe('DashboardConfigDatasource', () => {
  let component;
  beforeEach(() => {
    component = shallow(<DashboardConfigDatasource {...PROPS} />);
  });

  it('renders without crashing', () => {
    expect(component.exists()).toBe(true);
  });

  it('if props datasourceValue is empty button add is diasbled', () => {
    expect(component.find(Button).prop('disabled')).toBeTruthy();
  });

  it('if props datasourceValue.datadaousrce and datasourceCode are not empty button add is not diabled', () => {
    component = shallow(<DashboardConfigDatasource
      {...PROPS}
      datasourceValue={DATASOURCE_VALUE}
      datasourceCode="111222"
    />);
    expect(component.find(Button).prop('disabled')).toBeFalsy();
  });

  it('if props datasourceValue is not empty click button add  call the function push on the field props', () => {
    const props = {
      fields: {
        push: jest.fn(),
        remove: jest.fn(),
      },
      datasourceValue: DATASOURCE_VALUE,
      datasourceCode: 'xxx',
    };
    component = shallow(<DashboardConfigDatasource {...PROPS} {...props} />);
    const event = component.find(Button);
    expect(component.find(Button).prop('disabled')).toBeFalsy();
    event.simulate('click');
    expect(props.fields.push).toHaveBeenCalledWith(DATASOURCE_VALUE);
  });

  it('if props datasources is not empty show table ', () => {
    component = shallow(<DashboardConfigDatasource {...PROPS} datasources={DASHBOARD_LIST_DATASOURCE['1']} datasourceCode="" />);
    expect(component.find('table')).toHaveLength(1);
    expect(component.find('thead')).toHaveLength(1);
    expect(component.find('tr th')).toHaveLength(5);
    expect(component.find('tbody')).toHaveLength(1);
  });
});
