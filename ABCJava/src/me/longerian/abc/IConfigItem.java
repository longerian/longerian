package me.longerian.abc;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by longerian on 16/6/18.
 */
public interface IConfigItem {

    String SOURCE_TYPE_STATIC = "static";
    String SOURCE_TYPE_DYNAMIC = "dynamic";

    /**
     * Something to trigger pop-layer,two forms:
     * <ul>
     * <li>Activity qualified name- triggering pop-layer when corresponding activity resumed;
     * <li>PopLayer schema-triggering pop-layer when received corresponding broadcast
     * </ul>
     *
     * @return Activity qualified name or pop-layer schema
     */
    String getUri();

    String[] getUris();

    /**
     * @return Pop-layer H5's content URL
     */
    String getUrl();

    double getModalThreshold();

    String getUuid();

    String getDebugInfo();

    boolean ignoreTime();

    double getTimeoutWhenNext();

    long getStartTimeStamp();

    long getEndTimeStamp();

    void setJsonString(String json);

    boolean isEmbed();

    String getSourceType();

    JSONObject getExtra() throws JSONException;
}
