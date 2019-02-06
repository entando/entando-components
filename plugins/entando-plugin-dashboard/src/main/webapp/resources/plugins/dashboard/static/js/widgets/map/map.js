$(document).ready(() => {
  const OPTIONS = {
    url:
      "https://api.tiles.mapbox.com/v4/{id}/{z}/{x}/{y}.png?access_token=pk.eyJ1IjoibWFwYm94IiwiYSI6ImNpejY4NXVycTA2emYycXBndHRqcmZ3N3gifQ.rJcFIG214AriISLbB6B5aw",
    copyright: `Map data &copy; <a href="https://www.openstreetmap.org/">OpenStreetMap</a> contributors, <a href="https://creativecommons.org/licenses/by-sa/2.0/">CC-BY-SA</a>, '
      Imagery © <a href="https://www.mapbox.com/">Mapbox</a>`
  };

  class Map {
    constructor(id, latitude = 39.223841, longitude = 9.121661, zoom = 13) {
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
        layers: [streetsLayer, satelliteLayer]
      });
      this.baseMaps = {
        Satellite: satelliteLayer,
        Streets: streetsLayer
      };
    }

    addLayer(layer, options) {
      const newLayer = L.layerGroup();
      const {title, data} = layer;

      var greenIcon = L.icon({
        iconUrl: `../resources/plugins/dashboard/static/img/direction_up.png`,
        iconSize: [32, 37], // size of the icon
        iconAnchor: [22, 94], // point of the icon which will correspond to marker's location
        shadowAnchor: [4, 62], // the same for the shadow
        popupAnchor: [-3, -76] // point from which the popup should open relative to the iconAnchor
      });

      data.forEach(item => {
        const {street} = item;
        const lat = _.get(item, options.latitude);
        const lon = _.get(item, options.longitude);
        const info = _.get(item, options.information);
        if (lat !== null && lon !== null) {
          L.marker([lat, lon], {icon: greenIcon})
            .bindPopup(info)
            .addTo(newLayer);
        }
      });
      L.control.layers(this.baseMaps, {[title]: newLayer}).addTo(this.map);
    }
  }

  // main
  const map = new Map("map", 39.223841, 9.121661, 13);
  map.addLayer(datasources[0], {
    latitude: "street.latitude",
    longitude: "street.longitude",
    information: "street.name"
  });
});
