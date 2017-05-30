package me.longerian.abc.jsonprocessor;

import org.json.JSONArray;

/**
 * Created by longerian on 16/12/6.
 */
public class IsJsonArray implements Ruler {
    @Override
    public boolean matches(Object actualValue) {
        return actualValue instanceof JSONArray;
    }
}
