<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="wp" uri="/aps-core" %>

<script>
    const DEVICE1 = {
        status: "online",
        inUse: "On",
        batteryLevel: "90%",
        deviceCode: "xxx-33-444",
        deviceBrand: "logitech",
        expirationGuarantee: "31/12/2017",
        coordinates: ["39.2153109", "9.1076246"],
        information: "Parcheggi Piazza Matteoti"
    };
    const DEVICE2 = {
        status: "offline",
        inUse: "Off",
        batteryLevel: "70%",
        deviceCode: "xxx-55-444",
        deviceBrand: "logitech",
        expirationGuarantee: "31/12/2021",
        coordinates: ["39.2168495", "9.1075549"],
        information: "Piazza del Carmine"
    };
    const DEVICE3 = {
       status: "online",
        inUse: "On",
        batteryLevel: "off",
        deviceCode: "xxx-zz-444",
        deviceBrand: "asus",
        expirationGuarantee: "31/12/2018",
        coordinates: ["39.2136159", "9.115505"],
        information: "Via Roma"
    };
    const DEVICE4 = {
        status: "offline",
        inUse: "Off",
        batteryLevel: "",
        deviceCode: "xxx-22-444",
        deviceBrand: "intel",
        expirationGuarantee: "",
        coordinates: ["39.2153109", "9.1076246"],
        information: "Parcheggi Piazza Matteoti"
    };

    const DATASOURCE_PAYLOAD = {
        id: "parking",
        columns: [
            {key: "status", value: "status111", hidden: false},
            {key: "inUse", value: "inUse", hidden: false},
            {key: "batteryLevel", value: "batteryLevel", hidden: false},
            {key: "deviceCode", value: "deviceCode", hidden: false},
            {key: "deviceBrand", value: "deviceBrand", hidden: false},
            {key: "expirationGuarantee", value: "expirationG.", hidden: true},
            {key: "coordinates", value: "coordinates", hidden: false},
            {key: "information", value: "information", hidden: false}
        ],
        data: [DEVICE1, DEVICE2, DEVICE3, DEVICE4]
    };


    $(document).ready(() => {
        console.log('jQuery ready');
        let { columns, data } = DATASOURCE_PAYLOAD;
        let content = '<tr>';
        columns = columns.filter(f=> !f.hidden);
        columns.forEach(({value})=>{
            content += '<th>'+ value + '</th>';
          });
          content +='</tr>';
          $('#thead-dashboard-table').append(content);
          data.forEach((row) =>{
            let contentRow = '<tr>';
            columns.forEach(({key})=>{
              contentRow += '<td>'+ row[key] + '</td>';
              });
            contentRow += '</tr>';
            $('#tbody-dashboard-table').append(contentRow);
          })



    });

</script>


<div id="root-dashboard-table" class="container-fluid">
    <table class="table table-bordered">
        <thead id="thead-dashboard-table">
        </thead>
        <tbody id="tbody-dashboard-table">
        </tbody>
    </table>
</div>
