const org = {};
org.entando = org.entando || {};
org.entando.dashboard = org.entando.dashboard || {};
org.entando.dashboard.Table = class {
  constructor(id, config) {
    console.log("Table - config", config);
    const {columns, data} = config;
    console.log(data);
    this.id = id;
    const columnsDefs = columns.filter(f => !f.hidden);
    this.config = {
      data: data.reduce((acc, item) => {
        const cols = columnsDefs.reduce((acc1, col) => {
          acc1.push(item[col.key]);
          return acc1;
        }, []);
        //console.log("cols", cols);
        acc.push(cols);
        return acc;
      }, []),
      columns: columnsDefs.map(m => ({title: m.value}))
    };
  }
  show() {
    console.log("config table", this.config);
    return $(this.id).DataTable(this.config);
  }
};
