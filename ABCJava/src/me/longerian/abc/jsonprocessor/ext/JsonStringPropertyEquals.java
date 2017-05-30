package me.longerian.abc.jsonprocessor.ext;

import me.longerian.abc.jsonprocessor.Ruler;
import org.json.JSONObject;

/**
 * Created by longerian on 16/12/6.
 */
public class JsonStringPropertyEquals implements Ruler {

    private final String property;

    private final String targetValue;

    public JsonStringPropertyEquals(String property, String targetValue) {
        this.property = property;
        this.targetValue = targetValue;
    }

    @Override
    public boolean matches(Object actualValue) {
        if (actualValue instanceof JSONObject) {
            if (((JSONObject) actualValue).has(property)) {
                Object value = ((JSONObject) actualValue).opt(property);
                if (value == null) {
                    return targetValue == null;
                } else {
                    return value.equals(targetValue);
                }
            }
        }
        return false;
    }
}
