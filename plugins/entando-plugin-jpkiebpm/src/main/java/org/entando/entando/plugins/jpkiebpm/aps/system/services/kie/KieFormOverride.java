/*
* The MIT License
*
* Copyright 2017 Entando Inc..
*
* Permission is hereby granted, free of charge, to any person obtaining a copy
* of this software and associated documentation files (the "Software"), to deal
* in the Software without restriction, including without limitation the rights
* to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
* copies of the Software, and to permit persons to whom the Software is
* furnished to do so, subject to the following conditions:
*
* The above copyright notice and this permission notice shall be included in
* all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
* IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
* FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
* AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
* LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
* OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
* THE SOFTWARE.
*/
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie;

import java.util.Date;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.IBpmOverride;
import org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.model.override.OverrideList;

/**
 *
 * @author Entando
 */
public class KieFormOverride {

    /**
     * Add an overried to the current field
     *
     * @param override
     */
    public void addOverride(IBpmOverride override) {
        if (null == override) {
            return;
        }
        if (null == this.getOverrides()) {
            this.setOverrides(new OverrideList());
        }
        this.getOverrides().addOverride(override);
    }

	public int getId() {
		return _id;
	}
	public void setId(int id) {
		this._id = id;
	}

	public Date getDate() {
		return _date;
	}
	public void setDate(Date date) {
		this._date = date;
	}

	public String getField() {
		return _field;
	}
	public void setField(String field) {
		this._field = field;
	}

	public String getContainerId() {
		return _containerId;
	}
	public void setContainerId(String containerId) {
		this._containerId = containerId;
	}

	public String getProcessId() {
		return _processId;
	}
	public void setProcessId(String processId) {
		this._processId = processId;
	}

    public OverrideList getOverrides() {
        return _overrides;
    }

    public void setOverrides(OverrideList overrides) {
        this._overrides = overrides;
    }

	private int _id;
	private Date _date;
	private String _field;
	private String _containerId;
	private String _processId;
    // this object is de/serialized automatically
	private OverrideList _overrides;

}
