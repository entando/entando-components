import React, { Component } from 'react';
import { connect } from 'react-redux';
import * as action from '../state/actions/actions';
import queryString from 'query-string-parser';

class InfoPage extends Component {
  componentDidMount() {
    const parsed = queryString.fromQuery(window.location.search) || {};
    this.props.remoteGetTaskDetails(parsed.configId,parsed.containerId,parsed.taskId);
  }

  render() {
    const details = this.props.taskDetails.map(details => (
      <div className="col-md-4">
        <p><strong> {details.name} </strong> <br />
          {String(details.value)}
        </p>
      </div>
    ));

    return (
      <div id="bpm-task-details-box">
        <div className="ibox float-e-margins">
          <div id="bpm-task-details-ibox-content" className="ibox-content">
            <h1 id="bpm-task-details-title">Task details</h1>
            <div className="row">
              {details}
            </div>
          </div>
        </div>
      </div>
    );
  }
}

InfoPage.defaultProps = {
  taskDetails: [],
};

const mapStateToProps = state => ({
  taskDetails: state.taskDetailReducer.taskDetails || [],
  selectedTask: state.taskDetailReducer.selectedTask,
  loading: state.taskDetailReducer.loading,
  error: state.taskDetailReducer.error,
  accessToken: state.taskDetailReducer.accessToken,
});
const mapDispatchToProps = dispatch => ({
  remoteGetTaskDetails: (configId,containerId,taskId) => dispatch(action.remoteGetTaskDetails(configId,containerId,taskId)),
  selectTask: (taskid) => dispatch(action.selectTask(taskid)),
  setAccessToken: (accessToken) => dispatch(action.setAccessToken(accessToken)),
});
export default connect(mapStateToProps, mapDispatchToProps)(InfoPage);
