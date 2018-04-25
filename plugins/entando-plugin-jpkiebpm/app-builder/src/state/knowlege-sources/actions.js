import { initialize } from 'redux-form';
import { getKnowledgeSources } from 'api/knowledgeSources';

export const fetchKnowledgeSources = dispatch =>
  new Promise((resolve) => {
    getKnowledgeSources().then((response) => {
      response.json().then((data) => {
        if (response.ok) {
          dispatch(initialize('getKnowledgeSources', data.payload));
          resolve();
        } else {
          // TODO Handle api failure
          // dispatch(addErrors(data.errors.map(err => err.message)));
          resolve();
        }
      });
    });
  });

export default fetchKnowledgeSources;
