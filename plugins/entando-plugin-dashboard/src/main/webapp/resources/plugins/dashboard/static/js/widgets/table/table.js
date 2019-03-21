if (org === undefined) {
  var org = {};
}
org.entando = org.entando || {};
org.entando.dashboard = org.entando.dashboard || {};
org.entando.dashboard.Table = class {
  constructor(context, id, config) {
    console.log("Table - config", config);
    const {options, serverName, datasource, accessToken} = config;
    this.id = id;

    const columns = Object.keys(config.columns).reduce((acc, key) => {
      const obj = {
        data: key,
        title: config.columns[key].label,
        visible: !config.columns[key].hidden || false
      };
      acc.push(obj);
      return acc;
    }, []);

    this.config = {
      scrollY: 200,
      scrollCollapse: true,
      scroller: true,
      responsive: true,
      deferRender: true,
      processing: true,
      ajax: {
        url: `${context}api/plugins/dashboard/server/${serverName}/datasource/${datasource}/data`,
        type: "GET",
        contentType: "application/json",
        beforeSend: request => {
          if (config.accessToken) {
            request.setRequestHeader("Authorization", "Bearer " + accessToken);
          }
        },
        dataSrc: json => {
          console.log("json.payload", json.payload);
          return json.payload;
        }
      },
      columns
    };
    if (options && options.downlodable) {
      this.config.dom = "lBfrtip";
      this.config.buttons = ["csv", "excel", "pdf", "print"];
    }
  }
  show() {
    console.log(this.config);
    return new $(this.id).DataTable(this.config);
  }
};
