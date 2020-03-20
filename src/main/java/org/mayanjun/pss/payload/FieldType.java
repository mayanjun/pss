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

package org.mayanjun.pss.payload;

import org.mayanjun.pss.DeserializeException;
import org.mayanjun.pss.SerializeException;
import org.mayanjun.pss.util.BinaryUtils;
import org.mayanjun.pss.util.SerializeUtils;

import java.nio.ByteBuffer;
import java.nio.charset.Charset;
import java.util.Date;

public enum FieldType implements TypeSerializer {

    INT8(1, false, new TypeSerializer() {
        public byte[] serialize(Object value) throws SerializeException {
            return SerializeUtils.int8ToBytes(value);
        }

        @Override
        public Object deserialize(ByteBuffer buffer) throws DeserializeException {
            return buffer.get();
        }

        @Override
        public Object unsignedValue(Object value) {
            return value;
        }
    }),

    UINT8(1, true, new TypeSerializer() {
        @Override
        public byte[] serialize(Object value) throws SerializeException {
            return SerializeUtils.int8ToBytes(value);
        }

        @Override
        public Object deserialize(ByteBuffer buffer) throws DeserializeException {
            return buffer.get();
        }

        @Override
        public Object unsignedValue(Object value) {
            return BinaryUtils.uint8ToShort((Byte) value);
        }
    }),

    INT16(2, false, new TypeSerializer() {
        @Override
        public byte[] serialize(Object value) throws SerializeException {
            return SerializeUtils.int16ToBytes(value);
        }

        @Override
        public Object deserialize(ByteBuffer buffer) throws DeserializeException {
            return buffer.getShort();
        }

        @Override
        public Object unsignedValue(Object value) {
            return value;
        }
    }),

    UINT16(2, true, new TypeSerializer() {
        @Override
        public byte[] serialize(Object value) throws SerializeException {
            return SerializeUtils.int16ToBytes(value);
        }

        @Override
        public Object deserialize(ByteBuffer buffer) throws DeserializeException {
            return buffer.getShort();
        }

        @Override
        public Object unsignedValue(Object value) {
            return BinaryUtils.uint16ToInt((Short) value);
        }
    }),

    INT32(4, false, new TypeSerializer() {
        @Override
        public byte[] serialize(Object value) throws SerializeException {
            return SerializeUtils.int32ToBytes(value);
        }

        @Override
        public Object deserialize(ByteBuffer buffer) throws DeserializeException {
            return buffer.getInt();
        }

        @Override
        public Object unsignedValue(Object value) {
            return value;
        }
    }),

    UINT32(4, true, new TypeSerializer() {
        @Override
        public byte[] serialize(Object value) throws SerializeException {
            return SerializeUtils.int32ToBytes(value);
        }

        @Override
        public Object deserialize(ByteBuffer buffer) throws DeserializeException {
            return buffer.getInt();
        }

        @Override
        public Object unsignedValue(Object value) {
            return BinaryUtils.uint32ToLong((Integer) value);
        }
    }),

    INT64(8, false, new TypeSerializer() {
        @Override
        public byte[] serialize(Object value) throws SerializeException {
            return SerializeUtils.int64ToBytes(value);
        }

        @Override
        public Object deserialize(ByteBuffer buffer) throws DeserializeException {
            return buffer.getLong();
        }

        @Override
        public Object unsignedValue(Object value) {
            return value;
        }
    }),

    DATE(8, false, new TypeSerializer() {
        @Override
        public byte[] serialize(Object value) throws SerializeException {
            return SerializeUtils.dateToBytes(value);
        }

        @Override
        public Object deserialize(ByteBuffer buffer) throws DeserializeException {
            long ts = buffer.getLong();
            return new Date(ts);
        }

        @Override
        public Object unsignedValue(Object value) {
            return value;
        }
    }),

    FLOAT(4, false, new TypeSerializer() {
        @Override
        public byte[] serialize(Object value) throws SerializeException {
            return SerializeUtils.floatToBytes(value);
        }

        @Override
        public Object deserialize(ByteBuffer buffer) throws DeserializeException {
            return buffer.getFloat();
        }

        @Override
        public Object unsignedValue(Object value) {
            return value;
        }
    }),

    DOUBLE(8, false, new TypeSerializer() {
        @Override
        public byte[] serialize(Object value) throws SerializeException {
            return SerializeUtils.doubleToBytes(value);
        }

        @Override
        public Object deserialize(ByteBuffer buffer) throws DeserializeException {
            return buffer.getDouble();
        }

        @Override
        public Object unsignedValue(Object value) {
            return value;
        }
    }),

    BOOL(1, false,  new TypeSerializer() {
        @Override
        public byte[] serialize(Object value) throws SerializeException {
            return SerializeUtils.boolToBytes(value);
        }

        @Override
        public Object deserialize(ByteBuffer buffer) throws DeserializeException {
            byte v = buffer.get();
            return v > 0 ? Boolean.TRUE : Boolean.FALSE;
        }

        @Override
        public Object unsignedValue(Object value) {
            return value;
        }
    }),

    // -1 表示变长, 字符串和字节数组最多 2^16 - 1(65535)个字符
    // 字符串一律采用UTF-8编码
    STRING(-1, false, new TypeSerializer() {
        @Override
        public byte[] serialize(Object value) throws SerializeException {
            return SerializeUtils.stringToBytes(value);
        }

        @Override
        public Object deserialize(ByteBuffer buffer) throws DeserializeException {
            byte bytes[] = SerializeUtils.readLengthBytes(buffer);
            if (bytes == null) return null;
            return new String(bytes, Charset.forName("UTF-8"));
        }

        @Override
        public Object unsignedValue(Object value) {
            return value;
        }
    }),

    BYTES(-1, false, new TypeSerializer() {
        @Override
        public byte[] serialize(Object value) throws SerializeException {
            return SerializeUtils.bytesToBytes(value);
        }

        @Override
        public Object deserialize(ByteBuffer buffer) throws DeserializeException {
            return SerializeUtils.readLengthBytes(buffer);
        }

        @Override
        public Object unsignedValue(Object value) {
            return value;
        }
    });


    FieldType(int size, boolean unsigned, TypeSerializer serializer) {
        this.size = size;
        this.serializer = serializer;
        this.unsigned = unsigned;
    }

    @Override
    public byte[] serialize(Object value) throws SerializeException {
        return this.serializer.serialize(value);
    }

    @Override
    public Object deserialize(ByteBuffer buffer) throws DeserializeException {
        return serializer.deserialize(buffer);
    }

    @Override
    public Object unsignedValue(Object value) {
        return serializer.unsignedValue(value);
    }

    private boolean unsigned;

    /**
     * 类型字节长度
     */
    private int size;

    private TypeSerializer serializer;

    public int getSize() {
        return size;
    }

    public boolean isUnsigned() {
        return unsigned;
    }
}
