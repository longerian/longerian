package me.longerian.abc.jsonprocessor;

/**
 * Created by longerian on 16/12/6.
 */
public class IsLong implements Ruler {
    @Override
    public boolean matches(Object actualValue) {
        return actualValue instanceof Long;
    }
}
