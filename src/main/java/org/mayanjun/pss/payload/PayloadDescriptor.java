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


import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * PayloadDescriptor representing description of an object
 * @since 2020-03-21
 * @author mayanjun
 */
public class PayloadDescriptor implements Serializable {

    /**
     * Descriptor id or version
     */
    private int id;

    private String name;

    private String displayName;

    private String description;

    private List<FieldDescriptor> fieldDescriptors = new LinkedList<FieldDescriptor>();

    public PayloadDescriptor() {
    }

    public PayloadDescriptor(int id) {
        this.id = id;
    }

    public PayloadDescriptor(int id, String name, String displayName, String description) {
        this(id, name, displayName, description, null);
    }

    public PayloadDescriptor(int id, String name, String displayName, String description, List<FieldDescriptor> fieldDescriptors) {
        this.id = id;
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        setFieldDescriptors(fieldDescriptors);
    }

    public int nullFlagSize() {
        int size = this.fieldDescriptors.size();
        int mod8 = size % 8;
        int byteLen = (size - mod8) / 8 + (mod8 == 0 ? 0 : 1);
        return byteLen;
    }

    public byte[] nullFlagBytes() {
        byte bytes[] = new byte[nullFlagSize()];
        if(bytes.length > 0) {
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = 0x00;
            }
        }
        return bytes;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDisplayName() {
        return displayName;
    }

    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean addFieldDescriptor(FieldDescriptor fieldDescriptor) {
        if(fieldDescriptor.isValid()) {
            this.fieldDescriptors.add(fieldDescriptor);
            return true;
        }
        return false;
    }

    public List<FieldDescriptor> getFieldDescriptors() {
        return fieldDescriptors;
    }

    public void setFieldDescriptors(List<FieldDescriptor> fieldDescriptors) {
        if (fieldDescriptors != null) {
            this.fieldDescriptors = fieldDescriptors;
        }
    }
}