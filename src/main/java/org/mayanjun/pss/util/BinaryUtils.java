/*
 * Copyright 2016-2018 mayanjun.org
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.mayanjun.pss.util;

/**
 * 二进制转换工具
 * @author mayanjun
 * @since 2018/7/25
 */
public class BinaryUtils {

    private static final int LONG_SIZE = 8;

    private BinaryUtils() {
    }

    private static final char [] HEX_CHARS = {'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};

    public static String toHexString(byte b) {
        return new String(new char[]{
                HEX_CHARS[(b >>> 4) & 0xF],
                HEX_CHARS[(b >>> 0) & 0xF]
        });
    }

    public static String toHexString(byte bs[]) {
        return toHexString(bs, 8);
    }

    public static String toHexString(byte bs[], int width) {
        return toHexString(bs, width, null);
    }

    public static String toHexString(byte bs[], int width, String linePrefix) {
        StringBuilder builder = new StringBuilder();
        if (linePrefix != null) builder.append(linePrefix);
        for (int i = 0; i < bs.length; i++) {
            if (i % width == 0  && i > 1) {
                builder.append("\n");
                if (linePrefix != null) builder.append(linePrefix);
            }
            builder.append(BinaryUtils.toHexString(bs[i])).append(" ");
        }
        return builder.toString();
    }

    public static short uint8ToShort(byte uint8) {
        return (short) (uint8 & 0xFF);
    }

    public static int uint16ToInt(short data) {
        return data & 0xFFFF;
    }

    public static long uint32ToLong(int data) {
        return data & 0xFFFFFFFFL;
    }

    public static String toMacAddress(byte macBytes[]) {
        StringBuffer sb = new StringBuffer();
        for(int i  = 0; i < macBytes.length; i++) {
            String xx = String.format("%02x", macBytes[i]);
            sb.append(xx);
            if(i < macBytes.length - 1) sb.append(":");
        }
        return sb.toString();
    }

    public static long bytesToLong(byte bytes[]) {
        if (bytes.length == 0) return 0;
        int max = bytes.length;
        if (max > LONG_SIZE) max = LONG_SIZE;
        long origin = 0;
        for (int i = 0; i < max; i++) {
            origin = origin | bytes[0];
            origin = origin << LONG_SIZE;
        }
        return origin;
    }
}
