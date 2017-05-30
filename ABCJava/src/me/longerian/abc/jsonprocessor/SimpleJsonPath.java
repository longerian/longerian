package me.longerian.abc.jsonprocessor;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by longerian on 16/12/6.
 * 描述json节点路径:
 * data.arrow
 * data.cards[0].id
 * data.cards[0].items[0].style.ratio
 * data.cards[0].items[0].data[0].img2Url
 * [0].data[0].image
 */
public class SimpleJsonPath implements Path {

    private static final int STATE_COMMON = 0;
    private static final int STATE_ARRAY_START = 1;
    private static final int STATE_ARRAY_END = 2;

    private static final char DOT = '.';
    private static final char ARRAY_START = '[';
    private static final char ARRAY_END = ']';

    private Queue<String> fragment = new LinkedList<String>();

    private int state;

    private final String path;

    public SimpleJsonPath(String path) {
        this.path = path;
        compile(this.path);
    }

    private void compile(String path) {
        fragment.clear();
        StringBuilder sb = new StringBuilder();
        state = STATE_COMMON;
        for (int i = 0, length = path.length(); i < length; i++) {
            char c = path.charAt(i);
            switch (c) {
                case DOT:
                    if (state == STATE_ARRAY_START) {
                        sb.append(c);
                        break;
                    } else if (state == STATE_ARRAY_END) {
                        state = STATE_COMMON;
                        break;
                    } else {
                        fragment.offer(sb.toString());
                        sb.delete(0, sb.length());
                    }
                    break;
                case ARRAY_START:
                    if (state == STATE_COMMON) {
                        if (sb.length() > 0) {
                            fragment.offer(sb.toString());
                            sb.delete(0, sb.length());
                        }
                        state = STATE_ARRAY_START;
                    } else {
                        //error
                        return;
                    }
                    break;
                case ARRAY_END:
                    if (state == STATE_ARRAY_START) {
                        fragment.offer(sb.toString());
                        sb.delete(0, sb.length());
                        state = STATE_ARRAY_END;
                    } else {
                        //error
                        return;
                    }
                    break;
                default:
                    sb.append(c);
                    break;
            }
        }
        if (state == STATE_COMMON) {
            fragment.offer(sb.toString());
        }
    }

    @Override
    public String describeAsText() {
        return fragment.toString();
    }

    @Override
    public Object fetchNode(JSONObject jsonObject) {
        if (jsonObject != null) {
            String key = fragment.poll();
            if (key != null) {
                if (jsonObject.has(key)) {
                    Object subJson = jsonObject.opt(key);
                    if (!fragment.isEmpty()) {
                        if (subJson instanceof JSONArray) {
                            return fetchNode((JSONArray) subJson);
                        } else {
                            return fetchNode((JSONObject) subJson);
                        }
                    } else {
                        return subJson;
                    }
                }
            } else {
                return jsonObject;
            }
        }
        return null;
    }

    @Override
    public Object fetchNode(JSONArray jsonArray) {
        if (jsonArray != null) {
            String key = fragment.poll();
            if (key != null) {
                int index = -1;
                try {
                    index = Integer.parseInt(key);
                } catch (Exception e) {
                }
                if (index >= 0 && index < jsonArray.length()) {
                    Object subJson = jsonArray.opt(index);
                    if (!fragment.isEmpty()) {
                        if (subJson instanceof JSONArray) {
                            return fetchNode((JSONArray) subJson);
                        } else {
                            return fetchNode((JSONObject) subJson);
                        }
                    } else {
                        return subJson;
                    }
                } else {
                    return null;
                }
            } else {
                return jsonArray;
            }
        }
        return null;
    }

}
