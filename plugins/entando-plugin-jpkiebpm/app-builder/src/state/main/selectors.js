import { createSelector } from 'reselect';
import { getSearchParams } from '@entando/router';
import { name as id } from '../../../package.json';

export const getLocalState = state => state.plugins[id];

export const getReduxStatus = createSelector(
  [getLocalState],
  localState => localState.reduxStatus,
);


export const getServerConfigList = createSelector(
  [getLocalState],
  localState => localState.serverConfig,
);

export const getConnectionOutcomes = createSelector(
  [getLocalState],
  localState => localState.connectionOutcomes,
);


export const getPluginPage = createSelector(
  [getSearchParams],
  searchParams => searchParams.pluginPage || '',
);
