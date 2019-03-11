package org.entando.entando.plugins.jpkiebpm.aps.system.services.api.model.datatablerequest;

import java.util.List;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "request")
public class DataTableTaskListApiRequest {

    private Integer draw;
    private Integer start;
    private Integer length;
    private List<DataTableColumnRequest> columns;
    private DataTableOrderRequest order;
    private DataTableSearchRequest search;
    
    public Integer getDraw() {
        return draw;
    }

    public void setDraw(Integer draw) {
        this.draw = draw;
    }

    public Integer getStart() {
        return start;
    }

    public void setStart(Integer start) {
        this.start = start;
    }

    public Integer getLength() {
        return length;
    }

    public void setLength(Integer length) {
        this.length = length;       
    }

    public List<DataTableColumnRequest> getColumns() {
        return columns;
    }

    public void setColumns(List<DataTableColumnRequest> columns) {
        this.columns = columns;
    }

    public DataTableOrderRequest getOrder() {
        return order;
    }

    public void setOrder(DataTableOrderRequest order) {
        this.order = order;
    }      

    public DataTableSearchRequest getSearch() {
        return search;
    }

    public void setSearch(DataTableSearchRequest search) {
        this.search = search;
    }
  
}
