import { initialize } from 'redux-form';
import { getChannels } from 'api/channels';

export const fetchChannels = dispatch =>
  new Promise((resolve) => {
    getChannels().then((response) => {
      response.json().then((data) => {
        if (response.ok) {
          dispatch(initialize('channels', data.payload));
          resolve();
        } else {
          // TODO Handle api failure
          // dispatch(addErrors(data.errors.map(err => err.message)));
          resolve();
        }
      });
    });
  });

export default fetchChannels;
