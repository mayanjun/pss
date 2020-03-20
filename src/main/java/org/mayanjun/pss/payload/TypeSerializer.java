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

import java.nio.ByteBuffer;

/**
 * Serialize/Deserialize object
 */
interface TypeSerializer {

    /**
     * Convert the value to bytes
     * @param value
     * @return
     * @throws SerializeException
     */
    byte[] serialize(Object value) throws SerializeException;

    /**
     * Deserialize the buffer to Object. Such as {@linkplain Payload}
     * @param buffer
     * @return
     * @throws DeserializeException
     */
    Object deserialize(ByteBuffer buffer) throws DeserializeException;

    Object unsignedValue(Object value);
}