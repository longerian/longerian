package me.longerian.abc.jsonprocessor;

import org.json.JSONObject;

/**
 * Created by longerian on 16/12/6.
 */
public class HasProperty implements Ruler {

    private final String targetValue;

    public HasProperty(String targetValue) {
        this.targetValue = targetValue;
    }


    @Override
    public boolean matches(Object actualValue) {
        if (actualValue instanceof JSONObject) {
            return ((JSONObject) actualValue).has(targetValue);
        }
        return false;
    }
}
