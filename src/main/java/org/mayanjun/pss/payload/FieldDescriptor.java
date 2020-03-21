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
import java.util.HashMap;
import java.util.Map;

/**
 * Describe a field
 */
public class FieldDescriptor implements Serializable {

    // 字段的英文名称
    private String name;

    private String displayName;

    private String description;

    private FieldType type;

    private Map<String, Object> attributes;

    public FieldDescriptor() {
    }

    public FieldDescriptor(String name, String displayName, String description, FieldType type) {
        this.name = name;
        this.displayName = displayName;
        this.description = description;
        this.type = type;
    }

    public boolean isValid() {
        return this.name != null && type != null;
    }

    public FieldDescriptor(String name, FieldType type) {
        this.name = name;
        this.type = type;
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

    public FieldType getType() {
        return type;
    }

    public void setType(FieldType type) {
        this.type = type;
    }

    public Map<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(Map<String, Object> attributes) {
        this.attributes = attributes;
    }

    public Object attribute(String name) {
        if (this.attributes == null) return null;
        return this.attributes.get(name);
    }

    public boolean removeAttribute(String name) {
        if (this.attributes == null) return false;
        return this.attributes.remove(name) != null;
    }

    public FieldDescriptor attribute(String name, Object value) {
        if (this.attributes == null) {
            synchronized (this) {
                Map<String, Object> attrs = this.attributes;
                if (attrs == null) {
                    attrs = new HashMap<String, Object>();
                    this.attributes = attrs;
                }
            }
        }
        this.attributes.put(name, value);
        return this;
    }
}
