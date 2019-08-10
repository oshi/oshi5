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
package oshi.driver;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import oshi.driver.annotation.Fallback;

/**
 * A {@link ComponentDriver} is the root of the driver hierarchy. Containers can
 * call {@link #query(AttributeEnum)} or {@link #queryAll()} to have this class
 * update their state. There is a one-to-one correspondence between
 * {@link ComponentDriver}s and container objects.<br>
 * <br>
 * This class automatically registers all query methods in its subclasses. Query
 * methods in {@link ExtensionDriver}s can also be registered with
 * {@link #register(ExtensionDriver)}.
 */
public abstract class ComponentDriver {

    private static final Logger log = LoggerFactory.getLogger(ComponentDriver.class);

    /**
     * This class wraps a query method's {@link MethodHandle} with additional
     * metadata that's useful for ordering query handles within a
     * {@link QueryStack}.<br>
     * <br>
     * 
     * Note: this class has a natural ordering that is inconsistent with equals.
     */
    private static final class QueryHandle implements Comparable<QueryHandle> {

        /**
         * A handle on the method that performs the query.
         */
        public MethodHandle handle;

        /**
         * A fallback annotation for query ordering.
         */
        public Fallback fallback;

        /**
         * The class that contributed this query handle.
         */
        public Class<?> cls;

        /**
         * The method name of the query method.
         */
        public String name;

        public QueryHandle(MethodHandle handle, Class<?> cls, Method method) {
            this.handle = Objects.requireNonNull(handle);
            this.cls = Objects.requireNonNull(cls);
            this.name = method.getName();
            this.fallback = method.getAnnotation(Fallback.class);
        }

        @Override
        public int compareTo(QueryHandle other) {
            if (this == other)
                return 0;

            if (this.fallback != null) {
                if (this.fallback.value() == other.cls) {
                    if (this.fallback.method() != null) {
                        if (this.fallback.method().equals(other.name)) {
                            return -1;
                        }
                    } else {
                        return -1;
                    }
                }
            } else if (other.fallback != null) {
                if (other.fallback.value() == this.cls) {
                    if (other.fallback.method() != null) {
                        if (other.fallback.method().equals(this.name)) {
                            return +1;
                        }
                    } else {
                        return +1;
                    }
                }
            }

            // No ordering preference
            return 0;
        }
    }

    /**
     * A {@link QueryStack} is a collection of {@link QueryHandle}s that are
     * invoked sequentially upon query. A query attempt always begins with the
     * top handle and continues down the stack until a handle succeeds. The
     * stack can be configured to pop failed query handles if they are unlikely
     * to ever succeed.
     */
    private static final class QueryStack extends ArrayList<QueryHandle> {
    }

    /**
     * An association between attribute-enums and query stacks (lists of query
     * handles in descending priority order). A particular query handle can
     * exist in multiple stacks, but not more than once in each.
     */
    private Map<AttributeEnum, QueryStack> handles;

    /**
     * A list of extensions in case they are needed in the future.
     */
    private List<ExtensionDriver> extensions;

    public void initialize(List<ExtensionDriver> extensions) {
        if (this.handles != null || this.extensions != null)
            throw new IllegalArgumentException("The driver has already been initialized");

        this.extensions = Objects.requireNonNull(extensions);
        this.handles = new HashMap<>();

        registerDriver(this);
        this.extensions.forEach(this::registerDriver);

        // Reorder each stack
        handles.values().forEach(Collections::sort);
    }

    /**
     * Query the driver hierarchy for the value of the attributes corresponding
     * to the given enums.
     * 
     * @param attributes
     *            The attribute enums of the attributes to query
     */
    public void query(AttributeEnum... attributes) {
        Arrays.stream(attributes).map(handles::get).distinct().forEach(this::query);
    }

    /**
     * Query every attribute in the driver hierarchy.
     */
    public void queryAll() {
        handles.values().stream().distinct().forEach(this::query);
    }

    private void query(QueryStack stack) {
        if (stack == null)
            // TODO
            return;

        for (var queryHandle : stack) {
            try {
                queryHandle.handle.invoke();
                return;
            } catch (Throwable e) {
                // TODO log exception and continue
                // TODO remove if configured
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * Iterate through the methods of the given object and build a
     * {@link QueryHandle} for each one containing a query annotation.
     * 
     * @param driver
     *            The driver instance
     */
    private void registerDriver(Object driver) {
        Class<?> driverClass = driver.getClass();
        while (driverClass != Object.class) {
            for (Method m : driverClass.getDeclaredMethods()) {

                // Build a list of attributes defined in the annotations
                var attributes = Arrays.stream(m.getDeclaredAnnotations()).flatMap(annotation -> {
                    try {
                        return Arrays
                                .stream((AttributeEnum[]) annotation.getClass().getMethod("value").invoke(annotation));
                    } catch (NoSuchMethodException e) {
                        // Not the annotation we're looking for
                        return Arrays.stream(new AttributeEnum[0]);
                    } catch (Exception e) {
                        throw new RuntimeException("Failed to read query annotation", e);
                    }
                }).collect(Collectors.toList());

                if (attributes.size() > 0) {
                    // Build query handle
                    try {
                        m.setAccessible(true);
                        var handle = new QueryHandle(MethodHandles.lookup().unreflect(m).bindTo(driver), driverClass,
                                m);
                        var stack = new QueryStack();
                        stack.add(handle);

                        for (var attribute : attributes) {
                            handles.put(attribute, stack);
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Cannot access query method: " + m.getName(), e);
                    }
                }
            }

            driverClass = driverClass.getSuperclass();
        }
    }
}
