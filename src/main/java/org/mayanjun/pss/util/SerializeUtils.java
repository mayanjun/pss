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

import org.mayanjun.pss.SerializeException;

import java.nio.ByteBuffer;
import java.util.Date;

/**
 * Serialization utils
 */
public class SerializeUtils {

    private static final byte [] TRUE_BYTES = new byte []{1};
    private static final byte [] FALSE_BYTES = new byte []{0};
    public static final int MAX_FIELD_LENGTH = 0xFFFF - 2;

    private SerializeUtils() {
    }

    public static byte[] int8ToBytes(Object value) throws SerializeException {
        if (value == null) return null;
        if (value instanceof Number) {
            return new byte[] {((Number) value).byteValue()};
        } else {
            throw new SerializeException("Can't convert type: " + value.getClass().getCanonicalName() + " ==> to Byte bytes");
        }
    }

    public static byte[] int16ToBytes(Object value) throws SerializeException  {
        if (value == null) return null;
        if (value instanceof Number) {
            short s = ((Number) value).shortValue();
            return new byte[] {
                    (byte) ((s >>> 8) & 0xFF),
                    (byte) ((s >>> 0) & 0xFF)
            };
        } else {
            throw new SerializeException("Can't convert type: " + value.getClass().getCanonicalName() + " ==> to Short bytes");
        }
    }

    public static byte[] int32ToBytes(Object value) throws SerializeException  {
        if (value == null) return null;
        if (value instanceof Number) {
            int i = ((Number) value).intValue();
            return new byte[] {
                    (byte) ((i >>> 24) & 0xFF),
                    (byte) ((i >>> 16) & 0xFF),
                    (byte) ((i >>> 8) & 0xFF),
                    (byte) ((i >>> 0) & 0xFF)
            };
        } else {
            throw new SerializeException("Can't convert type: " + value.getClass().getCanonicalName() + " ==> to Integer bytes");
        }
    }

    public static byte[] int64ToBytes(Object value) throws SerializeException  {
        if (value == null) return null;
        if (value instanceof Number) {
            long i = ((Number) value).longValue();
            return new byte[] {
                    (byte) ((i >>> 56) & 0xFF),
                    (byte) ((i >>> 48) & 0xFF),
                    (byte) ((i >>> 40) & 0xFF),
                    (byte) ((i >>> 32) & 0xFF),
                    (byte) ((i >>> 24) & 0xFF),
                    (byte) ((i >>> 16) & 0xFF),
                    (byte) ((i >>> 8) & 0xFF),
                    (byte) ((i >>> 0) & 0xFF)
            };
        } else {
            throw new SerializeException("Can't convert type: " + value.getClass().getCanonicalName() + " ==> to Long bytes");
        }
    }

    public static byte[] dateToBytes(Object value) throws SerializeException {
        if (value == null) return null;
        if (value instanceof Number) {
            return int64ToBytes(value);
        } else if (value instanceof Date) {
            return int64ToBytes(((Date) value).getTime());
        } else {
            throw new SerializeException("Can't convert type: " + value.getClass().getCanonicalName() + " ==> to Long(date) bytes");
        }
    }


    public static byte[] floatToBytes(Object value) throws SerializeException {
        if (value == null) return null;

        if (value instanceof Number) {
            float f = ((Number) value).floatValue();
            return int32ToBytes(Float.floatToIntBits(f));
        } else {
            throw new SerializeException("Can't convert type: " + value.getClass().getCanonicalName() + " ==> to Float bytes");
        }
    }

    public static byte[] doubleToBytes(Object value) throws SerializeException {
        if (value == null) return null;
        if (value instanceof Number) {
            double f = ((Number) value).doubleValue();
            return int64ToBytes(Double.doubleToLongBits(f));
        } else {
            throw new SerializeException("Can't convert type: " + value.getClass().getCanonicalName() + " ==> to Double bytes");
        }
    }

    public static byte[] boolToBytes(Object value) throws SerializeException {
        if (value == null) return null;
        if (value instanceof Boolean) {
            if (Boolean.TRUE.equals(value)) {
                return TRUE_BYTES;
            } else {
                return FALSE_BYTES;
            }
        } else {
            throw new SerializeException("Can't convert type: " + value.getClass().getCanonicalName() + " ==> to Boolean bytes");
        }
    }

    /**
     * 如果长度超出 65533 就截断
     * @param value
     * @return
     * @throws SerializeException
     */
    public static byte[] stringToBytes(Object value) throws SerializeException {
        if (value == null) return toLengthBytes(null);
        try {
            if (value instanceof String) {
                byte bytes[] = value.toString().getBytes("UTF-8");
                if (bytes.length == 0) return toLengthBytes(null);
                return toLengthBytes(bytes);
            } else {
                throw new SerializeException("Can't convert type: " + value.getClass().getCanonicalName() + " ==> to String bytes");
            }
        } catch (Exception e) {
            throw new SerializeException(e);
        }
    }

    /**
     * 如果长度超出 65533 就截断
     * @param value
     * @return
     * @throws SerializeException
     */
    public static byte[] bytesToLengthBytes(Object value) throws SerializeException {
        if (value == null) return null;
        if (value instanceof byte[]) {
            byte bs[] = (byte[]) value;
            if (bs.length == 0) return toLengthBytes(null);
            return toLengthBytes(bs);
        } else {
            throw new SerializeException("Can't convert type: " + value.getClass().getCanonicalName() + " ==> to Bytes bytes");
        }
    }

    /**
     * 给数组加前加两个字节的长度
     * @param bytes
     * @return
     * @throws SerializeException
     */
    private static byte [] toLengthBytes(byte [] bytes) throws SerializeException {
        if (bytes == null) return null;
        int len = bytes.length;
        if (bytes.length > MAX_FIELD_LENGTH) len = MAX_FIELD_LENGTH;

        byte newBytes[] = new byte[bytes.length + 2];
        byte [] lenBytes = int16ToBytes(bytes.length);
        newBytes[0] = lenBytes[0];
        newBytes[1] = lenBytes[1];
        System.arraycopy(bytes, 0, newBytes, 2, len);
        return newBytes;
    }

    /**
     *
     * @param buffer
     * @return
     */
    public static byte [] readLengthBytes(ByteBuffer buffer) {
        short len = buffer.getShort();
        int ilen = BinaryUtils.uint16ToInt(len);
        if (ilen == 0) return null;
        byte bytes[] = new byte[ilen];
        buffer.get(bytes);
        return bytes;
    }
}
