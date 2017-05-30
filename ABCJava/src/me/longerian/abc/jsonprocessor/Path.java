package me.longerian.abc.jsonprocessor;

import org.json.JSONArray;
import org.json.JSONObject;

/**
 * Created by longerian on 16/12/6.
 *
 */
public interface Path {

    String describeAsText();

    Object fetchNode(JSONObject jsonObject);

    Object fetchNode(JSONArray jsonArray);

}
