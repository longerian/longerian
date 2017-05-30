package me.longerian.abc;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by longerian on 16/11/29.
 */
public class JSONTest {

    public static void main(String[] args) throws JSONException {
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("value", "");

        System.out.println(jsonObject.optDouble("value"));

        "".trim();

//        jsonObject.put("imgUrl", "//gw.alicdn.com/imgextra/i1/107/TB2Jmb2k5pnpuFjSZFIXXXh2VXa_!!107-0-yamato.jpg");
        jsonObject = new JSONObject("{\"errorCode\":0,\"imgUrl\":\"\\/\\/gw.alicdn.com\\/imgextra\\/i1\\/107\\/TB2Jmb2k5pnpuFjSZFIXXXh2VXa_!!107-0-yamato.jpg\"}");
        System.out.println(jsonObject);
    }

}
