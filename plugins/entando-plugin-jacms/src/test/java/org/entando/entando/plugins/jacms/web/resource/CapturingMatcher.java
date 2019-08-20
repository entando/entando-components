package org.entando.entando.plugins.jacms.web.resource;

import org.hamcrest.BaseMatcher;
import org.hamcrest.Description;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Matcher which always returns true, and at the same time, captures the
 * actual values passed in for matching. These can later be retrieved with a
 * call to {@link #getLastMatched()} or {@link #getAllMatched()}.
 */
public class CapturingMatcher extends BaseMatcher<Object> {

    private List<Object> matchedList = new ArrayList<>();

    @Override
    public boolean matches(Object matched) {
        matchedList.add(matched);
        return true;
    }

    @Override
    public void describeTo(Description description) {
        description.appendText("any object");
    }

    /**
     * The last value matched.
     */
    public Object getLastMatched() {
        return matchedList.get(matchedList.size() - 1);
    }

    /**
     * All the values matched, in the order they were requested for
     * matching.
     */
    public List<Object> getAllMatched() {
        return Collections.unmodifiableList(matchedList);
    }
}