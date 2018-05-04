import { createSelector } from 'reselect';

import { getLocalState } from 'state/main/selectors';


export const getDeploymentUnits = createSelector(
  [getLocalState],
  localState => localState.widgetConfig.deploymentUnits,
);

export const getCaseDefinitions = createSelector(
  [getLocalState],
  localState => localState.widgetConfig.caseDefinitions,
);
