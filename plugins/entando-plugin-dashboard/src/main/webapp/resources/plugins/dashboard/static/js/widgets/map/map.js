const OPTIONS = {
  url:
    "https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw",
  copyright: `Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, '
      Imagery © <a href="https://www.mapbox.com/">Mapbox</a>`
};

const PATH_IMG = "../resources/plugins/dashboard/static/img/";

class Map {
  constructor(
    id,
    latitude = 39.223841,
    longitude = 9.121661,
    zoom = 13,
    position
  ) {
    const streetsLayer = L.tileLayer(OPTIONS.url, {
      id: "mapbox.streets",
      attribution: OPTIONS.copyright
    });
    const satelliteLayer = L.tileLayer(OPTIONS.url, {
      id: "mapbox.satellite",
      attribution: OPTIONS.copyright
    });

    this.map = L.map(id, {
      center: [latitude, longitude],
      zoom: zoom,
      minZoom: 10,
      maxZoom: 18,
      layers: [streetsLayer, satelliteLayer],
      zoomControl: false
    });
    this.baseMaps = {
      Satellite: satelliteLayer,
      Streets: streetsLayer
    };

    this.controlZoom = new L.control.zoom({position}).addTo(this.map);
    this.control = new L.control.layers(this.baseMaps, null, {
      position
    }).addTo(this.map);
    this.layerGroup = new L.layerGroup();
  }

  getIcon(item, icon) {
    const test = (check, val) => {
      const str = `${check}`.replace(new RegExp("val", "g"), val);
      return new Function(
        `if ( ${str} ) { return true; }  else return false;`
      ).call(val);
    };

    let className = "default fa fa-map-marker";
    if (icon) {
      const {rules} = icon;
      if (icon.rules) {
        const {key, conditions} = rules;
        for (let condition of conditions) {
          className = test(condition.check, item[key])
            ? `${condition.result} ${icon.marker}`
            : className;
        }
      }
    } else {
      className = className.concact("fa fa-map-marker");
    }
    return L.divIcon({className: `marker-icon-${className} `});
  }

  addLayer(layer, options) {
    this.control.removeLayer(this.layerGroup);
    const {title, data} = layer;
    const icon = layer.icon;
    data.forEach(item => {
      const lat = _.get(item, options.latitude);
      const lon = _.get(item, options.longitude);
      const info = _.get(item, options.information);
      if (lat !== null && lon !== null) {
        L.marker([lat, lon], {
          icon: this.getIcon(item, icon)
        })
          .bindPopup(info)
          .addTo(this.layerGroup);
      }
    });

    this.control.addOverlay(this.layerGroup, [title]);
  }

  showData(datasources) {
    const hourRange = "7-10";
    loadTrafficData(
      `http://156.148.14.180:8080/crs4-smartmobility/api/jptraffic/traffics/alltraffic/lat/0/long/0/hour/${hourRange}`
    ).then(paking => {
      this.addLayer(CONFIG_MAP.datasources[0], {
        latitude: "street.latitude",
        longitude: "street.longitude",
        information: "street.name"
      });
    });
  }
}

$(document).ready(() => {
  // main
  console.log("eseguo la mappa");
  const map = new Map("map", 39.223841, 9.121661, 13, "topright");
  map.showData(CONFIG_MAP.datasources);
  setInterval(() => {
    map.showData(CONFIG_MAP.datasources);
  }, 3000);
});
