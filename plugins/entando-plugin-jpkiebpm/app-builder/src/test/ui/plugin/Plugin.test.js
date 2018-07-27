import React from 'react';

import { configure, shallow } from 'enzyme';
import Adapter from 'enzyme-adapter-react-16';
import Plugin from 'ui/plugin/components/Plugin';

configure({ adapter: new Adapter() });

it('renders without crashing', () => {
  const component = shallow(<Plugin />);
  expect(component.exists()).toEqual(true);
});
