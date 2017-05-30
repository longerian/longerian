package me.longerian.abc.jsonprocessor.ext;

import me.longerian.abc.jsonprocessor.Ruler;
import org.json.JSONObject;

/**
 * Created by longerian on 16/12/6.
 */
public class JsonNotContainsProperty implements Ruler {

    private final String property;

    public JsonNotContainsProperty(String property) {
        this.property = property;
    }

    @Override
    public boolean matches(Object actualValue) {
        if (actualValue instanceof JSONObject) {
            return !((JSONObject) actualValue).has(property);
        }
        return false;
    }
}
