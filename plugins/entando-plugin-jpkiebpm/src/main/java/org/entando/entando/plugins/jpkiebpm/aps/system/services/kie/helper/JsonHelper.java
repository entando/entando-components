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
package org.entando.entando.plugins.jpkiebpm.aps.system.services.kie.helper;

import java.util.Iterator;
import org.apache.commons.lang3.StringUtils;
import org.json.JSONArray;
import org.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class JsonHelper {

    private static final Logger logger = LoggerFactory.getLogger(JsonHelper.class);

    /**
     * Search recursively the given key in the JSON
     *
     * @param json
     * @param key
     * @return
     */
    public static Object findKey(final JSONObject json, final String key) {
        String[] keys;
        if (StringUtils.isBlank(key)
                || null == json) {
            return null;
        }
        // first search from
        if (json.has(key)) {
            return json.get(key);
        }
        keys = JSONObject.getNames(json);
        if (null == keys
                || keys.length == 0) {
            return null;
        }
        for (String id : keys) {
            Object obj = json.get(id);

            if (obj instanceof JSONObject) {
                Object res = findKey((JSONObject) obj, key);

                if (null != res) {
                    return res;
                }
            } else if (obj instanceof JSONArray) {
                for (int i = 0; i < ((JSONArray) obj).length(); i++) {
                    Object elem = ((JSONArray) obj).get(i);

                    if (elem instanceof JSONObject) {
                        Object res = findKey((JSONObject) elem, key);

                        if (null != res) {
                            return res;
                        }
                    }
                }
            } else {
                // do nothing
            }
        }
        return null;
    }

    /**
     * Replace the desired JSON key with the given value
     *
     * @param json
     * @param key
     * @param value
     * @return true if operation succeeded
     */
    public static boolean replaceKey(JSONObject json, final String key, final Object value) {
        String[] keys;
        boolean res = false;

        if (StringUtils.isBlank(key)
                || null == json) {
            return false;
        }
        // first search from
        if (json.has(key)) {
            json.put(key, value);
            return true;
        }
        keys = JSONObject.getNames(json);
        if (null == keys
                || keys.length == 0) {
            return false;
        }
        for (String id : keys) {
            Object obj = json.get(id);

            if (obj instanceof JSONObject) {
                if (replaceKey((JSONObject) obj, key, value)) {
                    return true;
                }
            } else if (obj instanceof JSONArray) {
                for (int i = 0; i < ((JSONArray) obj).length(); i++) {
                    Object elem = ((JSONArray) obj).get(i);

                    if (elem instanceof JSONObject) {
                        res = replaceKey((JSONObject) elem, key, value);
                    }
                }
            } else {
                // do nothing
            }
        }
        return res;
    }

    public static JSONObject replaceObject(JSONObject obj, String key, Object newValue) throws Exception {

        logger.debug("replace JSONObject {} with {}", key, newValue.toString());

        if (obj.has(key)) {
            obj.remove(key);
            obj.put(key, newValue);
        } else {
            Iterator iterator = obj.keys();
            String iteratorKey;
            while (iterator.hasNext()) {
                iteratorKey = (String) iterator.next();
                logger.debug("replaceObject iteratorKey: {} ", iteratorKey);
                if ((obj.optJSONArray(iteratorKey) == null) && (obj.optJSONObject(iteratorKey) == null)) {
                    if (iteratorKey.equals(key)) {
                        logger.debug("key {} found replace with new value {} ", iteratorKey, newValue);
                        obj.remove(iteratorKey);
                        obj.put(iteratorKey, newValue);
                        return obj;
                    }
                }
                if (obj.optJSONObject(iteratorKey) != null) {
                    replaceObject(obj.getJSONObject(iteratorKey), key, newValue);
                }
                if (obj.optJSONArray(iteratorKey) != null) {
                    JSONArray jArray = obj.getJSONArray(iteratorKey);
                    for (int i = 0; i < jArray.length(); i++) {
                        replaceObject(jArray.getJSONObject(i), key, newValue);
                    }
                }
            }
        }
        return obj;
    }
}
