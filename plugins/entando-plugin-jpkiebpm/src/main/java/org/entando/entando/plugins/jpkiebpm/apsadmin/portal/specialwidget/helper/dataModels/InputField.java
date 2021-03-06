/*
 * The MIT License
 *
 * Copyright 2018 Entando Inc..
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
package org.entando.entando.plugins.jpkiebpm.apsadmin.portal.specialwidget.helper.dataModels;

public class InputField {

    private String id;
    private String name;
    private String label;
    private String typePAM;
    private String typeHTML;
    private String value;
    private String code;
    private String binding;
    private String standaloneClassName;
    private String serializedFieldClassName;
    private String span;
    private boolean required;
    private boolean readOnly;
    private boolean validateOnChange;
    private boolean openRow;
    private boolean closeRow;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTypePAM() {
        return typePAM;
    }

    public void setTypePAM(String typePAM) {
        this.typePAM = typePAM;
    }

    public String getTypeHTML() {
        return typeHTML;
    }

    public void setTypeHTML(String typeHTML) {
        this.typeHTML = typeHTML;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public boolean getRequired() {
        return required;
    }

    public void setRequired(boolean required) {
        this.required = required;
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getBinding() {
        return binding;
    }

    public void setBinding(String binding) {
        this.binding = binding;
    }

    public String getStandaloneClassName() {
        return standaloneClassName;
    }

    public void setStandaloneClassName(String standaloneClassName) {
        this.standaloneClassName = standaloneClassName;
    }

    public String getSerializedFieldClassName() {
        return serializedFieldClassName;
    }

    public void setSerializedFieldClassName(String serializedFieldClassName) {
        this.serializedFieldClassName = serializedFieldClassName;
    }

    public boolean isRequired() {
        return required;
    }

    public boolean isValidateOnChange() {
        return validateOnChange;
    }

    public void setValidateOnChange(boolean validateOnChange) {
        this.validateOnChange = validateOnChange;
    }

    public String getSpan() {
        return span;
    }

    public void setSpan(String span) {
        this.span = span;
    }

    public boolean isOpenRow() {
        return openRow;
    }

    public void setOpenRow(boolean openRow) {
        this.openRow = openRow;
    }

    public boolean isCloseRow() {
        return closeRow;
    }

    public void setCloseRow(boolean closeRow) {
        this.closeRow = closeRow;
    }
}
