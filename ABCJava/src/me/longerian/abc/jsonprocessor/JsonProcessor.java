package me.longerian.abc.jsonprocessor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by longerian on 16/12/6.
 */
public class JsonProcessor {

    private List<Processor> registeredProcess = new ArrayList<>();

    public void register(Processor processor) {
        if (!registeredProcess.contains(processor)) {
            registeredProcess.add(processor);
        }
    }

    public void unregister(Processor processor) {
        registeredProcess.remove(processor);
    }

    public JSONObject process(JSONObject input) {
        JSONObject output = input;
        iterateJsonValue(input);
        return output;
    }

    public JSONArray process(JSONArray input) {
        JSONArray output = input;
        iterateJsonArray(input);
        return output;
    }

    private void iterateJsonValue(JSONObject json) {
        if (json == null) {
            return;
        }
        Iterator<?> keys = json.keys();
        while (keys.hasNext()) {
            String key = (String) keys.next();
            Object value = json.opt(key);
            iterateProcess(value);
            if (value instanceof JSONObject) {
                JSONObject subJson = (JSONObject) value;
                iterateJsonValue(subJson);
            } else if (value instanceof JSONArray) {
                JSONArray subJsonArray = (JSONArray) value;
                iterateJsonArray(subJsonArray);
            }
        }
    }

    private void iterateJsonArray(JSONArray array) {
        if (array == null) {
            return;
        }
        for (int i = 0, length = array.length(); i < length; i++) {
            Object value = array.opt(i);
            iterateProcess(value);
            if (value instanceof JSONObject) {
                JSONObject subJson = (JSONObject) value;
                iterateJsonValue(subJson);
            } else if (value instanceof JSONArray) {
                JSONArray subJsonArray = (JSONArray) value;
                iterateJsonArray(subJsonArray);
            }
        }
    }

    private void iterateProcess(Object input) {
        for (int i = 0, size = registeredProcess.size(); i < size; i++) {
            Processor processor = registeredProcess.get(i);
            Ruler ruler = processor.getRule();
            if (ruler.matches(input)) {
                processor.process(input);
            }
        }
    }


}
