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

import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.OutputStream;
import java.nio.ByteBuffer;
import java.util.List;

public class Payloads {

    private Payloads() {
    }

    /**
     * 序列化一个Payload
     * @param payloadDescriptor
     * @param payload
     * @return
     */
    public static byte[] serialize(PayloadDescriptor payloadDescriptor, Object payload) throws SerializeException {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        serialize(payloadDescriptor, payload, out);
        return out.toByteArray();
    }

    /**
     * 序列化Payload并写入输出流
     * @param payloadDescriptor
     * @param payload
     * @param outputStream
     * @throws SerializeException
     */
    public static void serialize(PayloadDescriptor payloadDescriptor, Object payload, OutputStream outputStream) throws SerializeException {
        List<FieldDescriptor> fields = payloadDescriptor.getFieldDescriptors();
        DataOutputStream out = new DataOutputStream(outputStream);

        try {
            // 写ID
            out.writeInt(payloadDescriptor.getId());
            for (FieldDescriptor field : fields) {
                Object value = PropertyUtils.getNestedProperty(payload, field.getName());
                byte bytes[] = field.getType().serialize(value);
                out.write(bytes, 0, bytes.length);
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
            for (FieldDescriptor field : fields) {
                FieldType type = field.getType();
                Object value = type.deserialize(buffer);
                if (type.isUnsigned()) {
                    pl.addDataField(new UnsignedDataField(field, value));
                } else {
                    pl.addDataField(new DataField(field, value));
                }
            }
            return pl;
        } catch (Exception e) {
            throw new DeserializeException(e);
        }
    }
}
