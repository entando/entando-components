/*
*
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
* This file is part of Entando software. 
* Entando is a free software; 
* You can redistribute it and/or modify it
* under the terms of the GNU General Public License (GPL) as published by the Free Software Foundation; version 2.
* 
* See the file License for the specific language governing permissions   
* and limitations under the License
* 
* 
* 
* Copyright 2013 Entando S.r.l. (http://www.entando.com) All rights reserved.
*
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
