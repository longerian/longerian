package me.longerian.abc.jsonprocessor;

/**
 * Created by longerian on 16/12/6.
 */
public interface Processor {

    Ruler getRule();

    void process(Object orgValue);

}
