import React from 'react';
import ReactDOM from 'react-dom';
import registerServiceWorker from './registerServiceWorker';

import { createStore, applyMiddleware, compose, combineReducers } from 'redux';
import { Provider } from 'react-redux';
import thunkMiddleware from 'redux-thunk'

import InfoPage from './components/InfoPage';
import taskDetailReducer from './state/reducers/reducers';

import './styles/style.css';

// import 'bootstrap/dist/css/bootstrap.css';
// import './../node_modules/bootstrap/dist/css/bootstrap.min.css';
// import './../node_modules/font-awesome/css/font-awesome.css';
// import './../node_modules/animate.css/animate.min.css';
// import './steps/css/main.css';
// import './css/cssoverride.css';
// import './css/yilicss.css';



const composeEnhancers = process.env.NODE_ENV === 'development' ? window.__REDUX_DEVTOOLS_EXTENSION_COMPOSE__ : null || compose;

const rootReducer = combineReducers({
    taskDetailReducer: taskDetailReducer,
});

const store = createStore(rootReducer, composeEnhancers(
    applyMiddleware(thunkMiddleware)
));


ReactDOM.render(
    <Provider store={store} >
        <InfoPage />
    </Provider>
    , document.getElementById('bpm-task-details'));
registerServiceWorker();
