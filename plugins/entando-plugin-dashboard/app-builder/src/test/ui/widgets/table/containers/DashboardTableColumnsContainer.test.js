import {
  mapStateToProps,
  mapDispatchToProps,
} from 'ui/widgets/table/containers/DashboardTableColumnsContainer';

import { updateDatasourceColumns } from 'state/main/actions';

jest.mock('state/main/actions');
jest.mock('state/main/selectors');

const dispatchMock = jest.fn();

describe('DashboardTableColumnsContainer', () => {
  let props;
  describe('mapStateToProps', () => {
    it('maps properties state in DashboardTableColumns', () => {
      props = mapStateToProps({});
      expect(props).toHaveProperty('columns');
    });
  });

  describe('mapDispatchToProps', () => {
    beforeEach(() => {
      props = mapDispatchToProps(dispatchMock);
    });

    it('should map the correct function properties', () => {
      expect(props.onMoveColumn).toBeDefined();
    });

    it('should dispatch an action if onMoveColumn is called', () => {
      props.onMoveColumn();
      expect(dispatchMock).toHaveBeenCalled();
      expect(updateDatasourceColumns).toHaveBeenCalled();
    });
  });
});
