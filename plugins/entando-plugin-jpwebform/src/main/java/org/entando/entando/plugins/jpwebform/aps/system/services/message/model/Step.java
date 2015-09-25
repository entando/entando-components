/*
 * Copyright 2015-Present Entando Inc. (http://www.entando.com) All rights reserved.
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
package org.entando.entando.plugins.jpwebform.aps.system.services.message.model;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author E.Santoboni
 */
public class Step {
	
	
	public Step() {
	}
	
	public Step(String code, boolean builtGui){
		this.setCode(code);
		this.setBuiltGui(builtGui);
	}

	
	@Override
	public Step clone() {
		Step clone = new Step();
		clone.setCode(this.getCode());
        clone.setBuiltGui(this.isBuiltGui());
		clone.setOgnlExpression(this.getOgnlExpression());
		//clone.setUserGui(this.getUserGui());
		//clone.setUserCss(this.getUserCss());
		//clone.setOrder(this.getOrder());
		//clone.setType(this.getType());
		if (null != this.getAttributeOrder()) {
			for (int i = 0; i < this.getAttributeOrder().size(); i++) {
				String attributeName = this.getAttributeOrder().get(i);
				AttributeConfig attribute = this.getAttributeConfigs().get(attributeName);
				clone.addAttributeConfig(attribute.getName(), attribute.isView());
			}
		}
		return clone;
	}
	
	public String getCode() {
		return _code;
	}
	public void setCode(String code) {
		this._code = code;
	}

	/*
	public int getOrder() {
	return _order;
	}
	public void setOrder(int order) {
	this._order = order;
	}
	public StepType getType() {
	return _type;
	}
	public void setType(StepType type) {
	this._type = type;
	}
	 */
	/*
	public String getUserGui() {
	return _userGui;
	}
	public void setUserGui(String userGui) {
	this._userGui = userGui;
	}
	public String getUserCss() {
	return _userCss;
	}
	public void setUserCss(String userCss) {
	this._userCss = userCss;
	}
	 */
	public String getOgnlExpression() {
		return _ognlExpression;
	}

	public void setOgnlExpression(String ognlExpression) {
		this._ognlExpression = ognlExpression;
	}
	
	public boolean isBuiltGui() {
		return _builtGui;
	}
	public void setBuiltGui(boolean builtGui) {
		this._builtGui = builtGui;
	}
	
	public void addAttributeConfig(String name, boolean view) {
		AttributeConfig attribute = new AttributeConfig();
		attribute.setName(name);
		attribute.setView(view);
		this.getAttributeOrder().add(name);
		this.getAttributeConfigs().put(name, attribute);
	}
        
	public void removeAttributeConfig(String name) {
		this.getAttributeOrder().remove(name);
		this.getAttributeConfigs().remove(name);
	}
	
	public List<String> getAttributeOrder() {
		return _attributeOrder;
	}
	public Map<String, AttributeConfig> getAttributeConfigs() {
		return _attributeConfigs;
	}

        @Override
        public int hashCode() {
            int hash = 7;
            hash = 37 * hash + (this._code != null ? this._code.hashCode() : 0);
            return hash;
        }
        
        @Override
        public boolean equals(Object obj) {
            if (obj == null) {
                return false;
            }
            if (getClass() != obj.getClass()) {
                return false;
            }
            final Step other = (Step) obj;
            if ((this._code == null) ? (other._code != null) : !this._code.equals(other._code)) {
                return false;
            }
            return true;
        }
	
        
        
	private String _code;
	//private int _order;
	//private StepType _type;
	//private String _userGui;
	//private String _userCss;
	private boolean _builtGui;
	private String _ognlExpression;
	private List<String> _attributeOrder = new ArrayList<String>();
	private Map<String, AttributeConfig> _attributeConfigs = new HashMap<String, AttributeConfig>();
	
	//public enum StepType {EDIT, SUMMARY, APPROVE}
	
        
        
	public class AttributeConfig {
		/*
		@Override
		public AttributeConfig clone() {
			AttributeConfig clone = new AttributeConfig();
			clone.setName(this.getName());
			clone.setView(this.isView());
			return clone;
		}
		*/
		public String getName() {
			return _name;
		}
		public void setName(String name) {
			this._name = name;
		}
		
		public boolean isView() {
			return _view;
		}
		public void setView(boolean view) {
			this._view = view;
		}
		
		private String _name;
		private boolean _view;
		
	}
	
}