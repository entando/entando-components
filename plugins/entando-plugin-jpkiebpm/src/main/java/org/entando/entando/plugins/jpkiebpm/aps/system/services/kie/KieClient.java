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

import org.apache.http.client.utils.URIBuilder;
import org.entando.entando.plugins.jprestapi.aps.core.IClient;
import org.entando.entando.plugins.jprestapi.aps.core.helper.RequestHelper;

import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.DEFAULT_POSRT;
import static org.entando.entando.plugins.jpkiebpm.aps.system.KieBpmSystemConstants.DEFAULT_SCHEMA;

/**
 *
 * @author Entando
 */
public class KieClient extends IClient {

    public KieClient() {
        // do nothing
    }


    public KieClient(KIEAuthenticationCredentials credentials) {
        this.credentials = credentials;
    }


    @Override
    protected void init() {
        // do nothing
    }

    public String getBaseUrl() {
        URIBuilder uri = new URIBuilder();

        uri.setScheme(schema);
        uri.setHost(hostname);
        uri.setPort(port);
        return RequestHelper.appendPath(uri.toString(), webapp);
    }

    public KIEAuthenticationCredentials getCredentials() {
        return credentials;
    }

    public void setCredentials(KIEAuthenticationCredentials credentials) {
        this.credentials = credentials;
    }

    public String getHostname() {
        return hostname;
    }

    public void setHostname(String hostname) {
        this.hostname = hostname;
    }

    public String getSchema() {
        return schema;
    }

    public void setSchema(String schema) {
        this.schema = schema;
    }

    public int getPort() {
        return port;
    }

    public void setPort(int port) {
        this.port = port;
    }

    public String getWebapp() {
        return webapp;
    }

    public void setWebapp(String webapp) {
        this.webapp = webapp;
    }

    public Integer getTimeoutMsec() {
        return timeoutMsec;
    }

    public void setTimeoutMsec(Integer timeoutMsec) {
        this.timeoutMsec = timeoutMsec;
    }

    private KIEAuthenticationCredentials credentials;
    private String hostname;
    private String schema = DEFAULT_SCHEMA;
    private int port = DEFAULT_POSRT;
    private String webapp;
    private Integer timeoutMsec;

}
