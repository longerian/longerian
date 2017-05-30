package me.longerian.abc;

import java.util.LinkedList;
import java.util.Queue;

/**
 * Created by longerian on 16/8/23.
 */
public class PathTest {

    private static final int STATE_COMMON = 0;
    private static final int STATE_ARRAY_START = 1;
    private static final int STATE_ARRAY_END = 2;

    private static final char DOT = '.';
    private static final char ARRAY_START = '[';
    private static final char ARRAY_END = ']';

    private static Queue<String> exprFragment = new LinkedList<String>();

    private static int state;

    private static void compile(String path) {
        exprFragment.clear();
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
                        exprFragment.offer(sb.toString());
                        sb.delete(0, sb.length());
                    }
                    break;
                case ARRAY_START:
                    if (state == STATE_COMMON) {
                        if (sb.length() > 0) {
                            exprFragment.offer(sb.toString());
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
                        exprFragment.offer(sb.toString());
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
            exprFragment.offer(sb.toString());
        }
    }

    public boolean hasNextFragment() {
        return !exprFragment.isEmpty();
    }

    public String nextFragment() {
        return exprFragment.poll();
    }

    public static void main(String[] args) {
        compile("$abv.dsf[0.0].hong[0]");
        System.out.println(exprFragment);
        compile("[0].data[0].image");
        System.out.println(exprFragment);
    }

}
