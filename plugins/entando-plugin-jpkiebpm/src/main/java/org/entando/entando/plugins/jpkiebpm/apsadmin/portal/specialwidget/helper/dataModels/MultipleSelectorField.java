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

import java.util.List;

public class MultipleSelectorField extends InputField {
    private List<SelectOption> options;
    private int maxElementsOnTitle;
    private int maxDropdownElements;
    private boolean allowClearSelection;   
    private boolean allowFilter;
           
    public List<SelectOption> getOptions() {
        return options;
    }

    public void setOptions(List<SelectOption> options) {
        this.options = options;
    }

    public int getMaxElementsOnTitle() {
        return maxElementsOnTitle;
    }

    public void setMaxElementsOnTitle(int maxElementsOnTitle) {
        this.maxElementsOnTitle = maxElementsOnTitle;
    }

    public int getMaxDropdownElements() {
        return maxDropdownElements;
    }

    public void setMaxDropdownElements(int maxDropdownElements) {
        this.maxDropdownElements = maxDropdownElements;
    }

    public boolean isAllowClearSelection() {
        return allowClearSelection;
    }

    public void setAllowClearSelection(boolean allowClearSelection) {
        this.allowClearSelection = allowClearSelection;
    }

    public boolean isAllowFilter() {
        return allowFilter;
    }

    public void setAllowFilter(boolean allowFilter) {
        this.allowFilter = allowFilter;
    }

    
}

