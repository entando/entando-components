//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.helper.dataModels;

import com.agiletec.aps.system.common.entity.model.attribute.BooleanAttribute;

public class CheckedAttribute extends BooleanAttribute {

    private String checked;

    public String getChecked(){
        if(this.getBooleanValue()!=null && this.getBooleanValue()){
            return "checked";
        }
        return "";
    }
}
