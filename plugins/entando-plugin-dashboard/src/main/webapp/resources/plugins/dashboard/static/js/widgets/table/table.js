if (org === undefined) {
  var org = {};
}
org.entando = org.entando || {};
org.entando.dashboard = org.entando.dashboard || {};
org.entando.dashboard.Table = class {
  constructor(id, config) {
    console.log("Table - config", config);
    const {data, options} = config;
    this.id = id;
    const columns = Object.keys(config.columns).reduce((acc, key) => {
      const obj = {
        value: config.columns[key].label,
        hidden: config.columns[key].hidden || false
      };
      acc.push(obj);
      return acc;
    }, []);

    const columnsDefs = columns.filter(f => !f.hidden);
    this.config = {
      data: data.reduce((acc, item) => {
        const cols = columnsDefs.reduce((acc1, col) => {
          acc1.push(item[col.key]);
          return acc1;
        }, []);
        acc.push(cols);
        return acc;
      }, []),
      columns: columnsDefs.map(m => ({title: m.value})),
      responsive: true,
      deferRender: true,
      scrollY: 200,
      scrollCollapse: true,
      scroller: true
    };
    if (options && options.downlodable) {
      this.config.dom = "Bfrtip";
      this.config.buttons = ["csv", "excel", "pdf", "print"];
    }
  }
  show() {
    console.log(this.config);
    return new $(this.id).DataTable(this.config);
  }
};
