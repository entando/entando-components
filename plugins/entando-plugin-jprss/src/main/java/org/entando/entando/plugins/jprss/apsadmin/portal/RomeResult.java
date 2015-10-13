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
package org.entando.entando.plugins.jprss.apsadmin.portal;

import com.opensymphony.xwork2.ActionInvocation;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedOutput;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.apache.struts2.dispatcher.StrutsResultSupport;

import java.io.Writer;

/**
 * A simple {@link org.apache.struts2.dispatcher.StrutsResultSupport} to output a <a href="https://rome.dev.java.net/">Rome</a> {@link com.sun.syndication.feed.synd.SyndFeed} object into a newsfeed.
 * <p/>
 * Has 4 parameters that can be set on the {@link com.opensymphony.xwork2.Result}.
 * <ul>
 * <li><b>feedName</b> (required): the expression to find the {@link com.sun.syndication.feed.synd.SyndFeed} on the value stack (eg. 'feed' will result in a call to 'getFeed()' on your Action.</li>
 * <li><b>mimeType</b> (optional, defaults to 'text/xml'): the preferred mime type.</li>
 * <li><b>encoding</b> (optional, defaults to the {@link com.sun.syndication.feed.synd.SyndFeed}'s encoding or falls back on the system): the preferred encoding (eg. UFT-8)
 * <li><b>feedType</b> (optional): the feed type.
 * <p>
 * Accepted feedType values are:
 * <ul>
 * <li>atom_0.3</li>
 * <li>atom_1.0</li>
 * <li>rss_0.90</li>
 * <li>rss_0.91N (RSS 0.91 Netscape)</li>
 * <li>rss_0.91U (RSS 0.91 Userland)</li>
 * <li>rss_0.92</li>
 * <li>rss_0.93</li>
 * <li>rss_0.94</li>
 * <li>rss_1.0</li>
 * <li>rss_2.0</li>
 * </ul>
 * </p>
 * </li>
 * </ul>
 * 
 * @author Philip Luppens
 * @author Fred Toth (encoding and mime type)
 * @version $Id$
 */
public class RomeResult extends StrutsResultSupport {
    
    private final static Log logger = LogFactory.getLog(RomeResult.class);

    private static final long serialVersionUID = -6638060951669685997L;

    private String feedName;                // must be set by the parameter
    private String feedType;                // see javadoc for a list of the supported values
    private String mimeType = "text/xml";   // the original default, probably always wrong.
    private String encoding;                // defaults to platform default. Should be set in feed.

    /**
     * Implementation of {@link org.apache.struts2.dispatcher.StrutsResultSupport#doExecute(String, com.opensymphony.xwork2.ActionInvocation)}
     * @param location final location (jsp page, action, etc)
     * @param actionInvocation the ActionInvocation
     * @throws Exception
     */
	@Override
    public void doExecute(String location, ActionInvocation actionInvocation) throws Exception {
        if (feedName == null) {
            // ack, we need this to find the feed on the stack, if not, blow up
            final String message = "Required parameter 'feedName' not found. "
                            + "Make sure you have the param tag set and "
                            + "the staticParams interceptor enabled in your interceptor stack.";
            logger.error(message); // log it in case the runtime exception gets swallowed
            throw new RuntimeException(message); // no point in continuing ..
        }

        // don't forget to set the content to the correct mimetype
        ServletActionContext.getResponse().setContentType(mimeType);
        // get the feed from the stack that can be found by the feedName
        SyndFeed feed = (SyndFeed) actionInvocation.getStack().findValue(feedName);

        if (feed != null) {
            if (encoding == null)
                encoding = feed.getEncoding();    // second choice is whatever the feed specifies
            if (encoding != null)
                ServletActionContext.getResponse().setCharacterEncoding(encoding);
            // If neither of the above work, we'll get the platform default.

            if (logger.isDebugEnabled())
                logger.debug("Found object on stack with expression '" + feedName + "': " + feed);

            if (feedType != null) // if the feedType is specified, we'll override the one set in the feed object
                feed.setFeedType(feedType);

            SyndFeedOutput output = new SyndFeedOutput();
            // we'll need the writer since Rome doesn't support writing to an outputStream yet
            Writer out = null;
            try {
                out = ServletActionContext.getResponse().getWriter();
                output.output(feed, out);
            } catch (Exception e) {
                // Woops, couldn't write the feed ?
                logger.error("Could not write the feed: " + e.getMessage(), e);
            } finally {
                // close the output writer (will flush automatically)
                if (out != null)
                    out.close();
            }
        } else {
            // woops .. no object found on the stack with that name ?
            final String message = "Did not find object on stack with name '" + feedName + "'";
            logger.error(message);
            throw new RuntimeException(message); // no point in continuing ..
        }
    }
    
    public void setFeedName(String feedName) {
        this.feedName = feedName;
    }

    public void setFeedType(String feedType) {
        this.feedType = feedType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }
	
    public void setEncoding(String encoding) {
        this.encoding = encoding;
    }
	
}