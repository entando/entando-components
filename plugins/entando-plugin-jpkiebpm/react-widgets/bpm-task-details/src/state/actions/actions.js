import request from "../../../node_modules/superagent/superagent";

export const GET_TASK_DETAILS = 'GET_TASK_DETAILS';
export const SELECTED_TASK = 'SELECTED_TASK';

export const ACCESS_TOKEN = 'ACCESS_TOKEN';

export const REST_CALL_START = 'REST_CALL_START';
export const REST_CALL_SUCCESS = 'REST_CALL_SUCCESS';
export const REST_CALL_FAIL = 'REST_CALL_FAIL';


// //ENTANDO DEPLOYMENT UNCOMMENT THIS

const APP_NAME = window.location.pathname.split('/')[1];
const GET_TASK_DETAILS_ENDPOINT = "http://" + window.location.host + "/" + APP_NAME + "/api/rs/en/jpkiebpm/taskData.json";

// NPM START UNCOMMENT THIS
//const GET_TASK_DETAILS_ENDPOINT = "http://localhost:8082/entando/api/rs/en/jpkiebpm/taskData.json";

export const getTaskDetails = (payload) => {
  return {
    type: GET_TASK_DETAILS,
    payload: payload,
  };
}

export const setAccessToken = (payload) => {
  return {
    type: ACCESS_TOKEN,
    payload: payload,
  };
}
export const selectTask = (payload) => {
  return {
    type: SELECTED_TASK,
    payload: payload,
  };
}

export const restCallSuccess = () => {
  return {
    type: REST_CALL_SUCCESS
  };
};
export const restCallStart = () => {
  return {
    type: REST_CALL_START
  };
};

export const restCallFail = (error) => {
  return {
    type: REST_CALL_FAIL,
    error: error
  };
};

export const remoteGetTaskDetails = (configId,containerId,taskId) => {
  return dispatch => {
    dispatch(restCallStart());

    var payloadArray = {};
    request
      .get(`${GET_TASK_DETAILS_ENDPOINT}?configId=${configId}&containerId=${containerId}&taskId=${taskId}`)
      .set('Content-Type', 'application/json')
      .then((res) => {
        dispatch(restCallSuccess());
        payloadArray = res.text;
        const resJS = JSON.parse(payloadArray)
        console.log(JSON.stringify(resJS));
        return dispatch({ type: GET_TASK_DETAILS, payload: resJS.response.result.mainForm.fields.fieldset.field });
      })
      .catch((err) => {
        var tmp = {};

        tmp.errorMessage = err.message;
        tmp.errorResponse = err.response;
        dispatch(restCallFail(tmp.errorResponse));
      });
  }
}
