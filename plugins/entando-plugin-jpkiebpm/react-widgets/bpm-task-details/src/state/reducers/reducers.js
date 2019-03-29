import { updateObject } from '../../shared/utility';
import * as actionTypes from '../actions/actions';

const initialState = {
    taskDetails: [],
    // caseDetails: null,
    selectedTask: null,
    // selectedCase: null,
    // loading: false,
    // error: false,
    // accessToken:"",
};

const taskDetails = (state, action) => {
    return updateObject(state, {taskDetails: action.payload});
};
// const caseDetails = (state, action) => {
//     return updateObject(state, {caseDetails: action.payload});
// };
const selectedTask = (state, action) => {
    return updateObject(state, {selectedTask: action.payload});
};
// const selectedCase = (state, action) => {
//     return updateObject(state, {selectedCase: action.payload});
// };
const restCallFail = (state, action) => {
    return updateObject(state, { loading: false, error: true });
};

const restCallStart = (state, action) => {
    return updateObject(state, { loading: true, error: false });
};

const restCallSuccess = (state, action) => {
    return updateObject(state, { loading: false, error: false });
};

const accessToken = (state, action) => {
    return updateObject(state, {accessToken: action.payload});
};

const taskDetailReducer = (state = initialState, action) => {
    switch (action.type) {
        case actionTypes.GET_TASK_DETAILS: return taskDetails(state, action);
//        case actionTypes.GET_CASE_DETAILS: return caseDetails(state, action);
        case actionTypes.SELECTED_TASK: return selectedTask(state, action);
//        case actionTypes.SELECTED_CASE: return selectedCase(state, action);
        case actionTypes.REST_CALL_START: return restCallStart(state, action);
        case actionTypes.REST_CALL_FAIL: return restCallFail(state, action);
        case actionTypes.REST_CALL_SUCCESS: return restCallSuccess(state, action);
        case actionTypes.ACCESS_TOKEN: return accessToken(state, action);
        default: return state;
    }
};

export default taskDetailReducer;
