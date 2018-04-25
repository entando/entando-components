import { makeRequest, METHODS } from '@entando/apimanager';

export const getKnowledgeSources = (params = '') => (
  makeRequest({
    uri: `/api/bpm/knowledgeSources${params}`,
    method: METHODS.GET,
    useAuthentication: true,
  })
);

export default getKnowledgeSources;
