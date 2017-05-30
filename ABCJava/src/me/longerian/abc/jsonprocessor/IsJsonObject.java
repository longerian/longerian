package me.longerian.abc.jsonprocessor;

import org.json.JSONObject;

/**
 * Created by longerian on 16/12/6.
 */
public class IsJsonObject implements Ruler {
    @Override
    public boolean matches(Object actualValue) {
        return actualValue instanceof JSONObject;
    }
}
