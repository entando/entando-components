import { createStore, compose, applyMiddleware, combineReducers } from 'redux';
import thunk from 'redux-thunk';
import rootReducer from 'state/main/reducer';

import { name } from '../../package.json';

const wrappedReducer = combineReducers({
  [name]: rootReducer,
});

const pluginsReducer = combineReducers({
  plugins: wrappedReducer,
});

const store = createStore(
  pluginsReducer,
  undefined, // preloaded state
  compose(
    applyMiddleware(thunk),
    // eslint-disable-next-line
    window.__REDUX_DEVTOOLS_EXTENSION__ && window.__REDUX_DEVTOOLS_EXTENSION__(),
  ),
);

export default store;
