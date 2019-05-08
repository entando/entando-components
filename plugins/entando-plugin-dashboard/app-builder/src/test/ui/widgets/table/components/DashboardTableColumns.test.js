import React from 'react';
import 'test/enzyme-init';
import { shallow } from 'enzyme';

import DashboardTableColumns from 'ui/widgets/table/components/DashboardTableColumns';

const props = {
  columns: [
    {
      column1: 'columns1',
      hidden: false,
    },
  ],
  onMoveColumn: jest.fn(),
  onShowHideColumn: jest.fn(),
};

describe('DashboardTable', () => {
  let component;
  beforeEach(() => {
    component = shallow(<DashboardTableColumns {...props} />);
  });

  it('renders without crashing', () => {
    expect(component.exists()).toBe(true);
  });
});
