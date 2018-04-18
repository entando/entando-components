import { createSelector } from 'reselect';
import { name as id } from '../../../package.json';

export const getLocalState = state => state.plugins[id];

export const getReduxStatus = createSelector(
  [getLocalState],
  localState => localState.reduxStatus,
);
