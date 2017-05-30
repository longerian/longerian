package me.longerian.abc.jsonprocessor;

import java.util.Arrays;
import java.util.List;

/**
 * Created by longerian on 16/12/6.
 */
public class AnyOf implements Ruler {

    private final List<Ruler> matchers;

    public AnyOf(List<Ruler> matchers) {
        this.matchers = matchers;
    }

    @Override
    public boolean matches(Object actualValue) {
        for (int i = 0, size = matchers.size(); i < size; i++) {
            boolean result = matchers.get(i).matches(actualValue);
            if (result) {
                return true;
            }
        }
        return false;
    }

    public static AllOf allOf(Ruler... rulers) {
        return new AllOf(Arrays.asList(rulers));
    }

}
