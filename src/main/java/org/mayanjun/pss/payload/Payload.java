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

import org.mayanjun.pss.util.BinaryUtils;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

/**
 * Represent  a payload
 * @since 2020-03-21
 * @author mayanjun
 */
public class Payload {

    private int descriptorId;

    private Map<String, FieldValue> dataFieldMap = new LinkedHashMap<String, FieldValue>();

    public Payload(int descriptorId) {
        this.descriptorId = descriptorId;
    }

    public Payload addDataField(FieldValue fieldValue) {
        if (fieldValue != null) {
            dataFieldMap.put(fieldValue.getFieldDescriptor().getName(), fieldValue);
        }
        return this;
    }

    public int getDescriptorId() {
        return descriptorId;
    }

    public Map<String, FieldValue> getDataFieldMap() {
        return dataFieldMap;
    }

    @Override
    public String toString() {
        return "Payload {" +
                "\n\tdescriptorId: " + descriptorId +
                ", \n\tdataFieldMap: {\n" + dataFieldMapToString() +
                "\t}\n" +
                '}';
    }

    private String dataFieldMapToString() {
        if (dataFieldMap == null) return null;
        if (dataFieldMap.isEmpty()) return "{}";
        Set<Map.Entry<String, FieldValue>> set = dataFieldMap.entrySet();
        StringBuilder sb = new StringBuilder();
        for (Map.Entry<String, FieldValue> ent : set) {
            FieldValue f = ent.getValue();
            Object value = f.getValue();
            sb.append("\t\t").append(ent.getKey()).append(": ")
                    .append("type=").append(f.getFieldDescriptor().getType());

            if (value instanceof byte[]) {
                byte bs[] = (byte[]) value;
                sb.append(", length=").append(bs.length).append(", value=\n")
                        .append(BinaryUtils.toHexString((byte[])value, 8, "\t\t\t"));
            } else {
                sb.append(", value=").append(value);
            }

            if(value == null) {
                sb.append("(null value)");
            } else {
                sb.append("(").append(f.getValue().getClass().getSimpleName()).append(")");
                if (f instanceof UnsignedFieldValue) {
                    Object uv = ((UnsignedFieldValue) f).getUnsignedValue();
                    sb.append(f instanceof UnsignedFieldValue ? ", unsignedValue=" + uv : "")
                            .append("(").append(uv.getClass().getSimpleName()).append(")");
                }
            }

            sb.append("\n");
        }
        return sb.toString();
    }
}