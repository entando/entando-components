import React from "react";
import {Row, Col, Button, Icon} from "patternfly-react";

import FormattedMessageLocal from "ui/i18n/FormattedMessage";

import DashboardConfigDatasourceStatusContainer from "ui/dashboard-config/common/Containers/DashboardConfigDatasourceStatusContainer";

const addDatasource = (fields, value) => {
  if (value.name !== undefined && value.uri !== undefined) {
    fields.push(value);
  }
};

const removeDatasource = (fields, index) => {
  fields.remove(index);
};

const DashboardConfigDatasource = ({
  fields,
  datasourceValue,
  datasources,
  page,
  pageSize,
  totalItems,
  changePage
}) => {
  return (
    <div className="DashboardConfigDatasource">
      <Row>
        <Col xs={12}>
          <div className="btn-toolbar pull-right">
            <Button
              className="DashboardConfigDatasource__datasource-btn"
              disabled={
                datasourceValue.name === undefined ||
                datasourceValue.uri === undefined
              }
              type="button"
              bsStyle="default"
              onClick={() => addDatasource(fields, datasourceValue)}
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
                  <th width="32%">
                    <FormattedMessageLocal id="plugin.config.datasource" />
                  </th>
                  <th width="32%">
                    <FormattedMessageLocal id="plugin.config.datasourceURI" />
                  </th>
                  <th>
                    <FormattedMessageLocal id="plugin.config.datasourceStatus" />
                  </th>
                  <th width="5%">&nbsp;</th>
                </tr>
              </thead>
              <tbody>
                {datasources.map((ds, index) => (
                  <tr key={`datasource-${index}`}>
                    <td>{ds.name}</td>
                    <td>{ds.uri}</td>
                    <td>
                      <DashboardConfigDatasourceStatusContainer />
                    </td>
                    <td className="text-center">
                      <Icon
                        type="fa"
                        size="lg"
                        name="trash"
                        onClick={() => removeDatasource(fields, index)}
                      />
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          </Col>
        )}
      </Row>
    </div>
  );
};

export default DashboardConfigDatasource;
