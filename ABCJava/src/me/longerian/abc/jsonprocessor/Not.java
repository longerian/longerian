package me.longerian.abc.jsonprocessor;

/**
 * Created by longerian on 16/12/6.
 */
public class Not implements Ruler {

    private final Ruler matcher;

    public Not(Ruler matcher) {
        this.matcher = matcher;
    }

    @Override
    public boolean matches(Object actualValue) {
        return !matcher.matches(actualValue);
    }

    public static Not notA(Ruler ruler) {
        return new Not(ruler);
    }
}
