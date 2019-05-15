import React from 'react';
import PropTypes from 'prop-types';

import { Label, Modal } from 'patternfly-react';

import FormattedMessageLocal from 'ui/i18n/FormattedMessage';

const DashboardConfigModalPreviewDatasource = ({
  isVisible, handleClose, onEnter, datasourceCode, previewColumns,
}) => (

  <Modal
    className="DashboardConfigModalPreviewDatasource"
    show={isVisible}
    onHide={() => handleClose()}
    onEnter={onEnter}
  >
    <Modal.Header closeButton>
      <Modal.Title>
        <FormattedMessageLocal id="plugin.config.previewDatasource" />
        <strong>{datasourceCode || null}</strong>
      </Modal.Title>
    </Modal.Header>
    <Modal.Body>
      <table className="DashboardConfigModalPreviewDatasource__table table table-striped table-bordered table-reponsive">
        <thead>
          <tr>
            <th>
              <FormattedMessageLocal id="plugin.config.previewColumn" />
            </th>
            <th>
              <FormattedMessageLocal id="plugin.config.previewType" />
            </th>
          </tr>
        </thead>
        <tbody>
          {
            previewColumns.map(col => (
              <tr key={`preview-datasource-${col.name}`}>
                <td>{col.name}</td>
                <td>{col.type}</td>
              </tr>
            ))
          }
        </tbody>
      </table>
    </Modal.Body>
  </Modal>


);

DashboardConfigModalPreviewDatasource.propTypes = {
  isVisible: PropTypes.bool,
  handleClose: PropTypes.func,
  onEnter: PropTypes.func,
  datasourceCode: PropTypes.string,
  previewColumns: PropTypes.arrayOf(PropTypes.shape({})),
};
DashboardConfigModalPreviewDatasource.defaultProps = {
  isVisible: false,
  handleClose: null,
  onEnter: null,
  datasourceCode: '',
  previewColumns: [],
};

export default DashboardConfigModalPreviewDatasource;
