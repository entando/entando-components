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
package com.agiletec.plugins.jpaddressbook.aps.system.services.vcard.parse;

/**
 * @author A.Cocco
 */
public class VCardAddress {

	/**
	 * Sets country
	 * @param country the country to set
	 */
	public void setCountry(String country) {
		this._country = country;
	}
	/**
	 * Returns country
	 * @return the country
	 */
	public String getCountry() {
		return _country;
	}

	/**
	 * Sets region
	 * @param region the region to set
	 */
	public void setRegion(String region) {
		this._region = region;
	}
	/**
	 * Returns region
	 * @return the region
	 */
	public String getRegion() {
		return _region;
	}
	/**
	 * Returns cap
	 * @return the cap
	 */
	public String getCap() {
		return _cap;
	}
	/**
	 * Sets cap
	 * @param cap the cap to set
	 */
	public void setCap(String cap) {
		this._cap = cap;
	}
	/**
	 * Returns street
	 * @return the street
	 */
	public String getStreet() {
		return _street;
	}
	/**
	 * Sets street
	 * @param street the street to set
	 */
	public void setStreet(String street) {
		this._street = street;
	}
	/**
	 * Returns city
	 * @return the city
	 */
	public String getCity() {
		return _city;
	}
	/**
	 * Sets city
	 * @param city the city to set
	 */
	public void setCity(String city) {
		this._city = city;
	}

	private String _country;
	private String _region;
	private String _cap;
	private String _street;
	private String _city;

}
