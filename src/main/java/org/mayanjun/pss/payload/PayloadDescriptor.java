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


import java.util.LinkedList;
import java.util.List;

public class PayloadDescriptor {

    /**
     * 描述符ID
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

    public PayloadDescriptor addFieldDescriptor(FieldDescriptor fieldDescriptor) {
        this.fieldDescriptors.add(fieldDescriptor);
        return this;
    }

    public List<FieldDescriptor> getFieldDescriptors() {
        return fieldDescriptors;
    }

    public void setFieldDescriptors(List<FieldDescriptor> fieldDescriptors) {
        this.fieldDescriptors = fieldDescriptors;
    }
}