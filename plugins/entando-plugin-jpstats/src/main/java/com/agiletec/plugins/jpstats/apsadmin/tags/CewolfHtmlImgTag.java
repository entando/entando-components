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
package com.agiletec.plugins.jpstats.apsadmin.tags;

import java.io.IOException;
import java.io.Writer;

public class CewolfHtmlImgTag extends CewolfAbstractHTMLBaseTag {
    
    /*
     public void writeTag(Writer writer) throws IOException {
        writer.write("<img ");
        writeAttributes(writer);
        writer.write("/>");
    }
     **/
    
    public void writeAttributes(Writer wr){
        try {
            super.writeAttributes(wr);
            appendAttributeDeclaration(wr, this.hSpace, "hspace");
            appendAttributeDeclaration(wr, this.height, "height");
            appendAttributeDeclaration(wr, this.vSpace, "vspace");
            appendAttributeDeclaration(wr, this.width, "width");
            appendAttributeDeclaration(wr, this.align, "align");
            appendAttributeDeclaration(wr, this.alt, "alt");
            appendAttributeDeclaration(wr, this.ismap, "ismap");
            appendAttributeDeclaration(wr, this.longDesc, "longdesc");
            appendAttributeDeclaration(wr, this.src, "src");
            appendAttributeDeclaration(wr, this.useMap, "usemap");
        } catch(IOException ioex){
            ioex.printStackTrace();
        }
    }
    
    protected void reset(){
        // width = UNDEFINED_INT;
        // height = UNDEFINED_INT;
        src = UNDEFINED_STR;
        alt = "";
        longDesc = UNDEFINED_STR;
        useMap = UNDEFINED_STR;
        ismap = UNDEFINED_STR;
        align = UNDEFINED_STR;
        hSpace = UNDEFINED_INT;
        vSpace = UNDEFINED_INT;
        forceSessionId = true;
        removeAfterRender = false;
        super.reset();
    }
    
    /** Setter for property width.
     * @param width New value of property width.
     */
    public void setWidth(int width) {
        this.width = width;
    }
    
    /** Setter for property height.
     * @param height New value of property height.
     */
    public void setHeight(int height) {
        this.height = height;
    }
    
    /** Setter for property src.
     * @param src New value of property src.
     */
    public void setSrc(String src) {
        this.src = src;
    }
    
    /** Setter for property alt.
     * @param alt New value of property alt.
     */
    public void setAlt(String alt) {
        this.alt = alt;
    }
    
    /** Setter for property longDesc.
     * @param longDesc New value of property longDesc.
     */
    public void setLongdesc(String longDesc) {
        this.longDesc = longDesc;
    }

    /** Setter for property useMap.
     * @param useMap New value of property useMap.
     */
    public void setUsemap(String useMap) {
        this.useMap = useMap;
    }
    
    /** Setter for property ismap.
     * @param ismap New value of property ismap.
     */
    public void setIsmap(String ismap) {
        this.ismap = ismap;
    }
    
    /** Setter for property align.
     * @param align New value of property align.
     */
    public void setAlign(String align) {
        this.align = align;
    }
    
    /** Setter for property hSpace.
     * @param hSpace New value of property hSpace.
     */
    public void setHspace(int hSpace) {
        this.hSpace = hSpace;
    }
    
    /** Setter for property vSpace.
     * @param vSpace New value of property vSpace.
     */
    public void setVspace(int vSpace) {
        this.vSpace = vSpace;
    }
    
    protected String getTagName() {
        return TAG_NAME;
    }
    
    protected boolean hasBody() {
        return false;
    }
    
    protected boolean wellFormed() {
        return true;
    }
    

    /**
     * @return Returns the forceSessionId.
     */
    public boolean isForceSessionId() {
    	return forceSessionId;
    }

    /**
     * @param forceSessionId The forceSessionId to set.
     */
    public void setForceSessionId(boolean forceSessionId) {
    	this.forceSessionId = forceSessionId;
    }

	/**
	 * @return Returns the removeAfterRender.
	 */
	public boolean isRemoveAfterRender() {
		return removeAfterRender;
	}

	/**
	 * @param removeAfterRender The removeAfterRender to set.
	 */
	public void setRemoveAfterRender(boolean removeAfterRender) {
		this.removeAfterRender = removeAfterRender;
	}
	
	  /** Holds value of property width. */
    protected int width = UNDEFINED_INT;
    
    /** Holds value of property height. */
    protected int height = UNDEFINED_INT;
    
    /** Holds value of property src. */
    protected String src = UNDEFINED_STR;
    
    /** Holds value of property alt. */
    protected String alt = "";
    
    /** Holds value of property longDesc. */
    protected String longDesc = UNDEFINED_STR;
    
    /** Holds value of property useMap. */
    protected String useMap = UNDEFINED_STR;
    
    /** Holds value of property ismap. */
    protected String ismap = UNDEFINED_STR;
    
    /** Holds value of property align. */
    protected String align = UNDEFINED_STR;
    
    /** Holds value of property hSpace. */
    protected int hSpace = UNDEFINED_INT;
    
    /** Holds value of property vSpace. */
    protected int vSpace = UNDEFINED_INT;
    
    /**
     * Add or not JSESSIONID
     */
    protected boolean forceSessionId = true;

    /**
     * Remove image from Storage after rendering
     */
    protected boolean removeAfterRender = false;
    
	private final static String TAG_NAME = "img";

}
