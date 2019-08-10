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
package oshi.api.internal;

import java.util.Arrays;

import oshi.api.AttributeKey;
import oshi.api.Container;
import oshi.driver.AttributeEnum;
import oshi.driver.ComponentDriver;

/**
 * A superclass for all generated container classes.
 */
public abstract class AbstractContainer implements Container {

    /**
     * The root driver instance associated with this container. If {@code null},
     * this container is considered to be <b>DETACHED</b> and all attempts to
     * update its attributes will fail. Otherwise, the container is considered
     * to be <b>ATTACHED</b>.
     */
    protected transient ComponentDriver driver;

    @Override
    public void query(AttributeKey<?>... keys) {
        // Translate attribute-keys into attribute-enums for the driver
        driver.query(Arrays.stream(keys).map(key -> key.getAttributeEnum()).toArray(AttributeEnum[]::new));
    }
}
