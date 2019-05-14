import React, { Component } from 'react';
import PropTypes from 'prop-types';
import { Row, Col, Button, DropdownKebab, MenuItem } from 'patternfly-react';
import FormattedMessageLocal from 'ui/i18n/FormattedMessage';
import DashboardConfigDatasourceStatus from 'ui/dashboard-config/common/components/DashboardConfigDatasourceStatus';
import DashboardConfigModalPreviewDatasource from 'ui/dashboard-config/common/components/DashboardConfigModalPreviewDatasource';

import { isUndefined, get } from 'lodash';

const addDatasource = (fields, obj, callback) => {
  const {
    datasourceCode,
    datasourceValue: { datasource, datasourceURI },
  } = obj;

  if ((!isUndefined(datasourceCode) && !isUndefined(datasource)) || fields.length > 0) {
    fields.push({ datasourceCode, datasource, datasourceURI });
    if (callback) {
      callback(datasourceCode);
    }
  }
};

const removeDatasource = (fields, index) => {
  fields.remove(index);
};

class DashboardConfigDatasource extends Component {
  constructor(props) {
    super(props);
    this.state = {
      isVisibleModalPreviewDatasource: false,
      datasourceCode: '',
    };
    this.handleModal = this.handleModal.bind(this);
  }

  handleModal(datasourceCode) {
    this.setState(prevState => ({
      isVisibleModalPreviewDatasource: !prevState.isVisibleModalPreviewDatasource,
      datasourceCode,
    }));
  }

  render() {
    const {
      fields,
      datasourceValue,
      datasources,
      datasourceCode,
      testConnection,
      previewDatasource,
      previewColumns,
      datasourceCheck,
    } = this.props;
    return (
      <div className="DashboardConfigDatasource">
        <Row>
          <Col xs={12}>
            <div className="btn-toolbar pull-right">
              <Button
                className="DashboardConfigDatasource__datasource-btn"
                disabled={
                datasourceValue.datasource === undefined ||
                datasourceCode === undefined
              }
                type="button"
                bsStyle="default"
                onClick={() =>
                addDatasource(fields, { datasourceCode, datasourceValue }, testConnection)
              }
              >
                <FormattedMessageLocal id="common.add" />
              </Button>
            </div>
          </Col>
          {datasources.length === 0 ? null : (
            <Col xs={12}>
              <table className="DashboardConfigDatasource__table table table-striped table-bordered table-reponsive">
                <thead>
                  <tr>
                    <th width="20%">
                      <FormattedMessageLocal id="plugin.config.datasourceCode" />
                    </th>
                    <th>
                      <FormattedMessageLocal id="plugin.config.datasource" />
                    </th>
                    <th width="35%">
                      <FormattedMessageLocal id="plugin.config.datasourceURI" />
                    </th>
                    <th width="10%">
                      <FormattedMessageLocal id="plugin.config.datasourceStatus" />
                    </th>
                    <th width="5%">
                      <FormattedMessageLocal id="common.actions" />
                    </th>
                  </tr>
                </thead>
                <tbody>
                  {datasources.map((ds, index) => (
                    <tr key={`datasource-${ds.datasourceCode}`}>
                      <td>{ds.datasourceCode}</td>
                      <td>{ds.datasource}</td>
                      <td>{ds.datasourceURI}</td>
                      <td>
                        <DashboardConfigDatasourceStatus
                          status={datasourceCheck[ds.datasourceCode]}
                        />
                      </td>
                      <td className="text-center">
                        <DropdownKebab id="kebab-datadource">
                          <MenuItem
                            className="DashboardConfigDatasource__menu-item-test"
                            onClick={() => testConnection(ds.datasourceCode)}
                          >
                            <FormattedMessageLocal id="common.test" />
                          </MenuItem>
                          <MenuItem
                            className="DashboardConfigDatasource__menu-item-preview"
                            onClick={() => this.handleModal(ds.datasourceCode)}
                            disabled={get(datasourceCheck[ds.datasourceCode], 'status') === 'offline'}
                          >
                            <FormattedMessageLocal id="common.preview" />
                          </MenuItem>

                          <MenuItem
                            className="DashboardConfigDatasource__menu-item-remove"
                            onClick={() => removeDatasource(fields, index)}
                          >
                            <FormattedMessageLocal id="common.remove" />
                          </MenuItem>
                        </DropdownKebab>
                      </td>
                    </tr>
                ))}
                </tbody>
              </table>
            </Col>
        )}
        </Row>
        <DashboardConfigModalPreviewDatasource
          isVisible={this.state.isVisibleModalPreviewDatasource}
          handleClose={this.handleModal}
          onEnter={() => previewDatasource(this.state.datasourceCode)}
          datasourceCode={this.state.datasourceCode}
          previewColumns={previewColumns}
        />
      </div>
    );
  }
}


const DATASOURCE_TYPE = {
  datasource: PropTypes.string,
  datasourceURI: PropTypes.string,
};
DashboardConfigDatasource.propTypes = {
  datasourceValue: PropTypes.shape(DATASOURCE_TYPE),
  datasourceCode: PropTypes.string.isRequired,
  datasources: PropTypes.arrayOf(PropTypes.shape(DATASOURCE_TYPE)),
  fields: PropTypes.shape({}),
  testConnection: PropTypes.func.isRequired,
  previewDatasource: PropTypes.func.isRequired,
  previewColumns: PropTypes.arrayOf(PropTypes.shape({})),
  datasourceCheck: PropTypes.shape({}),
};
DashboardConfigDatasource.defaultProps = {
  datasourceValue: {
    datasource: undefined,
    datasourceURI: undefined,
  },
  datasources: [],
  fields: {},
  previewColumns: [],
  datasourceCheck: {},
};
export default DashboardConfigDatasource;
