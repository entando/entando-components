package org.entando.entando.plugins.jacms.web.contentsettings.model;

import lombok.Builder;
import lombok.NoArgsConstructor;

import java.util.Date;

@Builder
public class LastReloadInfoDto {

    private Date date;
    private int result;

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public int getResult() {
        return result;
    }

    public void setResult(int result) {
        this.result = result;
    }
}
