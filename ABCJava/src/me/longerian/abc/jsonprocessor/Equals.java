package me.longerian.abc.jsonprocessor;

/**
 * Created by longerian on 16/12/6.
 */
public class Equals implements Ruler {

    private final Object targetValue;

    public Equals(Object targetValue) {
        this.targetValue = targetValue;
    }

    @Override
    public boolean matches(Object actualValue) {
        return areEqual(actualValue, targetValue);
    }

    private static boolean areEqual(Object actual, Object expected) {
        if (actual == null) {
            return expected == null;
        }
        return actual.equals(expected);
    }

}
