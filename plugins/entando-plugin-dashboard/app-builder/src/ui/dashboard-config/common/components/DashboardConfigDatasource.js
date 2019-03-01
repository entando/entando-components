import React from "react";
import PropTypes from "prop-types";
import {Row, Col, Button, Icon} from "patternfly-react";
import FormattedMessageLocal from "ui/i18n/FormattedMessage";
import DashboardConfigDatasourceStatusContainer from "ui/dashboard-config/common/containers/DashboardConfigDatasourceStatusContainer";

const addDatasource = (fields, value) => {
  if (value.datasource !== undefined && value.datasourceURI !== undefined) {
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
}) => (
  <div className="DashboardConfigDatasource">
    <Row>
      <Col xs={12}>
        <div className="btn-toolbar pull-right">
          <Button
            className="DashboardConfigDatasource__datasource-btn"
            disabled={
              datasourceValue.datasource === undefined ||
              datasourceValue.datasourceURI === undefined
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
                  <td>{ds.datasource}</td>
                  <td>{ds.datasourceURI}</td>
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
const DATASOURCE_TYPE = {
  datasource: PropTypes.string,
  datasourceURI: PropTypes.string
};
DashboardConfigDatasource.PropType = {
  datasourceValue: PropTypes.shape(DATASOURCE_TYPE),
  datasources: PropTypes.arrayOf(PropTypes.shape(DATASOURCE_TYPE))
};
DashboardConfigDatasource.defaultProps = {
  datasourceValue: {
    datasource: undefined,
    datasourceURI: undefined
  },
  datasources: []
};
export default DashboardConfigDatasource;
