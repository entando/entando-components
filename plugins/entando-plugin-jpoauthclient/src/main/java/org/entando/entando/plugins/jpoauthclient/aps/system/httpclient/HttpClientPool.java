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
package org.entando.entando.plugins.jpoauthclient.aps.system.httpclient;

import java.net.URL;
import org.apache.commons.httpclient.HttpClient;

/**
 * A source of Jakarta Commons HttpClient objects.
 * 
 * @author John Kristian
 */
public interface HttpClientPool {

    /** Get the appropriate HttpClient for sending a request to the given URL. */
    public HttpClient getHttpClient(URL server);

}
