/**
 * OSHI (https://github.com/oshi/oshi)
 *
 * Copyright (c) 2010 - 2019 The OSHI Project Team:
 * https://github.com/oshi/oshi/graphs/contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package oshi.api;

import java.io.Serializable;

import oshi.driver.ComponentDriver;

/**
 * A container class represents a particular instance of a general system
 * component. Container classes have two main purposes:
 * <ul>
 * <li>Store the most recently fetched attribute values so the user doesn't have
 * to manually.</li>
 * <li>Provide a way for users to fetch (update) attribute values of the
 * corresponding component.</li>
 * </ul>
 */
public interface Container extends Serializable {

    public abstract void attach(ComponentDriver driver);

    /**
     * Update the values of the attributes corresponding to the given
     * {@link AttributeKey}s. Unlike sequentially calling the {@code query*}
     * methods for each attribute, this method doesn't duplicate batched queries
     * if possible.<br>
     * <br>
     * For example,
     * 
     * <pre>
     * disk.queryReadBytes();
     * disk.queryWriteBytes();
     * </pre>
     * 
     * causes each attribute to be updated twice because {@code READ_BYTES} and
     * {@code WRITE_BYTES} are fetched at the same time (batch queries).<br>
     * <br>
     * This method prevents wasted queries with the following usage:
     * 
     * <pre>
     * disk.query(READ_BYTES, WRITE_BYTES);
     * </pre>
     * 
     * 
     * @param keys
     *            One or more keys that uniquely identify the attributes to be
     *            updated
     */
    public abstract void query(AttributeKey<?>... keys);

    /**
     * Return the last-fetched value of the attribute corresponding to the given
     * {@link AttributeKey}.
     * 
     * @param <T>
     *            The attribute's data type
     * @param key
     *            A key that uniquely identifies the requested attribute
     * @return The attribute's value
     */
    public abstract <T> T get(AttributeKey<T> key);
}
