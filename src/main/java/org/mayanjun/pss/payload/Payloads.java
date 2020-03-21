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

import org.apache.commons.beanutils.PropertyUtils;
import org.mayanjun.pss.DeserializeException;
import org.mayanjun.pss.SerializeException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;

/**
 * Payloads utils
 * @since 2020-03-21
 * @author mayanjun
 */
public class Payloads {

    private static final Logger LOG = LoggerFactory.getLogger(Payloads.class);

    private Payloads() {
    }

    /**
     * 序列化一个Payload
     * @param payloadDescriptor pd
     * @param payload payload object
     * @return
     */
    public static byte[] serialize(PayloadDescriptor payloadDescriptor, Object payload) throws SerializeException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        serialize(payloadDescriptor, payload, out);
        return out.toByteArray();
    }


    private static void setNullBitFlag( byte nullFlagBytes[], int index) {
        int size = index + 1;
        int mod = size % 8;
        int b0 = (size - mod) / 8;
        int b1 = (mod == 0 ? 0 : 1);
        int bytesLen = b0 + b1;
        int byteIndex = bytesLen - 1;
        int bitIndex = index % 8;
        byte setByte = (byte) (0x01 << bitIndex);
        nullFlagBytes[byteIndex] = (byte) (nullFlagBytes[byteIndex] | setByte);
    }

    private static boolean isNullFlagSet(byte nullFlagBytes[], int index) {
        int size = index + 1;
        int mod = size % 8;
        int b0 = (size - mod) / 8;
        int b1 = (mod == 0 ? 0 : 1);
        int bytesLen = b0 + b1;
        int byteIndex = bytesLen - 1;
        int bitIndex = index % 8;
        byte setByte = (byte) (0x01 << bitIndex);
        return (nullFlagBytes[byteIndex] & setByte) == setByte;
    }

    /**
     * 序列化Payload并写入输出流
     * @param payloadDescriptor pd
     * @param payload payload
     * @param outputStream output stream
     * @throws SerializeException
     */
    public static void serialize(PayloadDescriptor payloadDescriptor, Object payload, OutputStream outputStream) throws SerializeException {
        List<FieldDescriptor> fields = payloadDescriptor.getFieldDescriptors();
        DataOutputStream out = new DataOutputStream(outputStream);

        try {
            // 写ID
            out.writeInt(payloadDescriptor.getId());

            // 获取能表示null字段的字节数组
            byte nullFlagBytes[] = payloadDescriptor.nullFlagBytes();

            Object values[] = new Object[fields.size()];
            for (int i = 0; i < fields.size(); i++) {
                FieldDescriptor fd = fields.get(i);
                Object value = PropertyUtils.getNestedProperty(payload, fd.getName());
                byte bytes[] = fd.getType().serialize(value);
                if (bytes == null) {
                    setNullBitFlag(nullFlagBytes, i);
                }
                values[i] = bytes;
            }

            // write null field flags
            out.write(nullFlagBytes, 0, nullFlagBytes.length);
            for (int i = 0; i < values.length; i++) {
                byte[] bytes = (byte[]) values[i];
                if(bytes != null) {
                    out.write(bytes, 0, bytes.length);
                }
            }
            out.flush();
        } catch (SerializeException e) {
            throw e;
        }
        catch (Exception e) {
            throw new SerializeException(e);
        }
    }

    public static Payload deserialize(PayloadDescriptor payloadDescriptor, byte [] payload) throws DeserializeException {
        List<FieldDescriptor> fields = payloadDescriptor.getFieldDescriptors();
        try {
            ByteBuffer buffer = ByteBuffer.wrap(payload);

            // 读ID
            int id = buffer.getInt();
            Payload pl = new Payload(id);

            // Read null flags
            byte nullFlagBytes[] = payloadDescriptor.nullFlagBytes();
            buffer.get(nullFlagBytes);

            int fieldSize = fields.size();
            for (int i = 0; i < fieldSize; i++) {
                FieldDescriptor fd = fields.get(i);
                FieldType type = fd.getType();

                // judge is null
                if (!isNullFlagSet(nullFlagBytes, i)) {
                    Object value = type.deserialize(buffer);
                    if (type.isUnsigned()) {
                        pl.addDataField(new UnsignedFieldValue(fd, value));
                    } else {
                        pl.addDataField(new FieldValue(fd, value));
                    }
                } else {
                    pl.addDataField(new FieldValue(fd, null));
                }
            }
            return pl;
        } catch (Exception e) {
            throw new DeserializeException(e);
        }
    }
}
