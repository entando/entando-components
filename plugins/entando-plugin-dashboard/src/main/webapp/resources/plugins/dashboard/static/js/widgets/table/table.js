if (org === undefined) {
  var org = {};
}
org.entando = org.entando || {};
org.entando.dashboard = org.entando.dashboard || {};
org.entando.dashboard.Table = class {
  constructor(context, id, config) {
    console.log("Table - config", config);

    const {options, serverName, datasource, accessToken, lang} = config;
    this.id = id;

    const columns = Object.keys(config.columns).reduce((acc, key) => {
      const obj = {
        data: key,
        title: config.columns[key].label,
        visible: !config.columns[key].hidden || false,
        isDate: config.columns[key].isDate
      };
      acc.push(obj);
      return acc;
    }, []);

    const language = lang === "it" ? Italian : English;

    this.config = {
      scrollY: 200,
      scrollCollapse: true,
      scroller: true,
      responsive: true,
      language,
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
          const dateColumns = columns.filter(f => f.isDate).map(m => m.data);
          const options = {
            day:'2-digit',
            month:'2-digit',
            year:'numeric',
            hour:'2-digit',
            minute:'2-digit',
            second:'2-digit'
          };
          json.payload.map((m, index) => {
            dateColumns.forEach(col => {
              json.payload[index][col] = new Date(
                json.payload[index][col]
              ).toLocaleDateString('it-IT',options);
            });
          });
          return json.payload || [];
        },
        error: xhr => {
          const str = `status: ${xhr.status} text: ${xhr.statusText} error: ${
            xhr.responseJSON.errors[0].message
          }`;
          alert(str);
        }
      },
      columns
    };
    if (options && options.downlodable) {
      this.config.dom = "Bfrtip";
      this.config.buttons = ["csv", "excel", "pdf", "print"];
    }
  }
  show() {
    return new $(this.id).DataTable(this.config);
  }
};
