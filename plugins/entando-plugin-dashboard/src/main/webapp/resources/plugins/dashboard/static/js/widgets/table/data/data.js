const CONFIG_DATA = {
  DATASOURCE_PAYLOAD: {
    id: "parking",
    columns: [
      {key: "status", value: "stato", hidden: false},
      {key: "inUse", value: "in Use", hidden: false},
      {key: "batteryLevel", value: "battery Level", hidden: false},
      {key: "deviceCode", value: "deviceCode", hidden: false},
      {key: "deviceBrand", value: "deviceBrand", hidden: false},
      {key: "expirationGuarantee", value: "expirationG.", hidden: true},
      {key: "coordinates", value: "coordinates", hidden: false},
      {key: "information", value: "information", hidden: false}
    ],
    data: [
      {
        status: "online",
        inUse: "On",
        batteryLevel: "90%",
        deviceCode: "xxx-33-444",
        deviceBrand: "logitech",
        expirationGuarantee: "31/12/2017",
        coordinates: ["39.2153109", "9.1076246"],
        information: "Parcheggi Piazza Matteoti"
      },
      {
        status: "offline",
        inUse: "Off",
        batteryLevel: "70%",
        deviceCode: "xxx-55-444",
        deviceBrand: "logitech",
        expirationGuarantee: "31/12/2021",
        coordinates: ["39.2168495", "9.1075549"],
        information: "Piazza del Carmine"
      },
      {
        status: "online",
        inUse: "On",
        batteryLevel: "off",
        deviceCode: "xxx-zz-444",
        deviceBrand: "asus",
        expirationGuarantee: "31/12/2018",
        coordinates: ["39.2136159", "9.115505"],
        information: "Via Roma"
      },
      {
        status: "offline",
        inUse: "Off",
        batteryLevel: "",
        deviceCode: "xxx-22-444",
        deviceBrand: "intel",
        expirationGuarantee: "",
        coordinates: ["39.2153109", "9.1076246"],
        information: "Parcheggi Piazza Matteoti"
      }
    ]
  }
};
