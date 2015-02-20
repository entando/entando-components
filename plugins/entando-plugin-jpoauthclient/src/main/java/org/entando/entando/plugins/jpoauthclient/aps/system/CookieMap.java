/*
 * Copyright 2007 Netflix, Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.entando.entando.plugins.jpoauthclient.aps.system;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Represents the set of cookies for the client of an HTTP request. Map-like
 * operations enable examining and modifying cookies.
 * 
 * @author John Kristian
 */
public class CookieMap {

    public CookieMap(HttpServletRequest request, HttpServletResponse response) {
        this.response = response;
        this.path = request.getContextPath();
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (int i = 0; i < cookies.length; i++) {
                Cookie cookie = cookies[i];
                if (cookie != null) {
                    name2value.put(cookie.getName(), cookie.getValue());
                }
            }
        }
    }
	
    public String get(String name) {
        return name2value.get(name);
    }
    
    public void put(String name, String value) {
        this.put(name, value, null);
    }
    
    public void put(String name, String value, Integer maxAge) {
        if (value == null) {
            remove(name);
        } else if (!value.equals(name2value.get(name))) {
            Cookie c = new Cookie(name, value);
            c.setPath(path);
            if (null != maxAge && maxAge.intValue() > -1) {
                //c.setMaxAge(365*24*60*60);//one year
                c.setMaxAge(maxAge.intValue());
            }
            response.addCookie(c);
            name2value.put(name, value);
        }
    }
    
    public void remove(String name) {
        if (name2value.containsKey(name)) {
            Cookie c = new Cookie(name, "");
            c.setMaxAge(0);
            c.setPath(path);
            response.addCookie(c);
            name2value.remove(name);
        }
    }

    public Set<String> keySet() {
        Set<String> set = Collections.unmodifiableSet(name2value.keySet());
        return set;
    }
	
    public String toString() {
        return name2value.toString();
    }
	
    private final HttpServletResponse response;

    private final String path;

    private final Map<String, String> name2value = new HashMap<String, String>();
	
}
