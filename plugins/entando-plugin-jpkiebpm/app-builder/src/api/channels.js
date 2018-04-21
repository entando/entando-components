import { makeRequest, METHODS } from '@entando/apimanager';

export const getChannels = (params = '') => (
  makeRequest({
    uri: `/api/bpm/channels${params}`,
    method: METHODS.GET,
    useAuthentication: true,
  })
);

export default getChannels;
