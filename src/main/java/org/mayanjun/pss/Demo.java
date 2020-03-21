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

package org.mayanjun.pss;

import org.mayanjun.pss.payload.*;
import org.mayanjun.pss.util.BinaryUtils;
import org.mayanjun.pss.util.JSON;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class Demo {


    public static void main(String[] args) throws Exception {
        test();
    }

    public static void test() throws Exception {
        PayloadDescriptor d = new PayloadDescriptor(12, "TestBean", "Test Bean", "Test description");
        d.addFieldDescriptor(new FieldDescriptor("age", FieldType.INT8));
        d.addFieldDescriptor(new FieldDescriptor("uage", FieldType.UINT8));
        d.addFieldDescriptor(new FieldDescriptor("ok", FieldType.BOOL));
        d.addFieldDescriptor(new FieldDescriptor("name", FieldType.STRING));
        d.addFieldDescriptor(new FieldDescriptor("height", FieldType.FLOAT));
        d.addFieldDescriptor(new FieldDescriptor("score", FieldType.DOUBLE));
        d.addFieldDescriptor(new FieldDescriptor("length", FieldType.INT64));
        d.addFieldDescriptor(new FieldDescriptor("test_int16", FieldType.INT16));
        d.addFieldDescriptor(new FieldDescriptor("test_uint16", FieldType.UINT16));
        d.addFieldDescriptor(new FieldDescriptor("test_int32", FieldType.INT32));
        d.addFieldDescriptor(new FieldDescriptor("test_uint32", FieldType.UINT32));
        d.addFieldDescriptor(new FieldDescriptor("date", FieldType.DATE));
        d.addFieldDescriptor(new FieldDescriptor("bytes", FieldType.BYTES));

        String json = JSON.format(d);
        //System.out.println("PayloadDescriptor:\n");
        //System.out.println(json);

        PayloadDescriptor pd = JSON.de(json, PayloadDescriptor.class);

        Map<String, Object> data = new HashMap<String, Object>();
        data.put("age", 17);
        data.put("uage", 222);
        data.put("ok", true);
        data.put("name", "mayanjun中国万岁");
        data.put("height", 564.3374);
        data.put("score", 22.4321);
        data.put("length", 2345);
        data.put("test_int16", Short.MAX_VALUE);
        data.put("test_uint16", Short.MAX_VALUE + 100);
        data.put("test_int32", Integer.MAX_VALUE);
        data.put("test_uint32", Integer.MAX_VALUE + 100);
        byte bss [] =  "aa".getBytes("utf-8");
        data.put("bytes", bss);
        Date now = new Date();
        data.put("date", now);

        byte bs[] = se(pd, data);

        de(pd, bs);
    }

    private static byte[] se(PayloadDescriptor d, Object object) throws Exception {
        byte payload [] = Payloads.serialize(d, object);
        System.out.println(
                String.format("---------- SERIALIZE(%d) ---------", payload.length)
        );
        System.out.println(BinaryUtils.toHexString(payload, 8));
        return payload;
    }

    private static void de(PayloadDescriptor d, byte bytes[]) throws  Exception {
        // 反序列化
        Payload pl = Payloads.deserialize(d, bytes);
        System.out.println("---------- DESERIALIZE ---------");
        System.out.println(pl);
    }

}
