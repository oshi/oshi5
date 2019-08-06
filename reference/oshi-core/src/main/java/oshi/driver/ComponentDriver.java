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
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * A {@link ComponentDriver} is the root of the driver hierarchy. Containers can
 * call {@link #query(Enum)} or {@link #queryAll()} to have this class update
 * their state. There is a one-to-one correspondence between
 * {@link ComponentDriver}s and container objects.<br>
 * <br>
 * This class automatically registers all query methods in its subclasses. Query
 * methods in {@link ExtensionDriver}s can also be registered with
 * {@link #register(ExtensionDriver)}.
 */
public class ComponentDriver {

    private static final class QueryHandle implements Comparable<QueryHandle> {
        public MethodHandle handle;
        public Fallback fallback;
        public Class<?> cls;

        public QueryHandle(MethodHandle handle, Fallback fallback, Class<?> cls) {
            this.handle = handle;
            this.fallback = fallback;
            this.cls = cls;
        }

        @Override
        public int compareTo(QueryHandle o) {
            // TODO implement query ordering with fallbacks
            return 0;
        }
    }

    /**
     * An association between attribute enums and lists of query handles in
     * descending priority order. A particular query handle can exist in
     * multiple lists, but not more than once in each list.
     */
    private Map<Enum<?>, List<QueryHandle>> handles;

    /**
     * A list of extensions in case they are needed in the future.
     */
    private List<ExtensionDriver> extensions;

    public ComponentDriver() {
        this.handles = new HashMap<>();
        this.extensions = new LinkedList<>();

        registerDriver(this);
    }

    /**
     * Query the driver hierarchy for the value of the attribute corresponding
     * to the given enum.
     * 
     * @param attribute
     *            The attribute enum of the attribute to query
     */
    public void query(Enum<?> attribute) {

        var h = handles.get(attribute);
        if (h == null) {
            // TODO
        }

        for (var t : h) {
            try {
                t.handle.invoke();
                return;
            } catch (Throwable e) {
                // TODO log exception and continue
                continue;
            }
        }
    }

    /**
     * Query every attribute in the driver hierarchy.
     */
    public void queryAll() {
        // Keep track of the handles that have been queried to prevent repeats
        var queried = new HashSet<MethodHandle>();
        for (var handleList : handles.values()) {
            for (var h : handleList) {
                if (queried.contains(h.handle))
                    break;

                queried.add(h.handle);
                try {
                    h.handle.invoke();
                    break;
                } catch (Throwable e) {
                    // TODO log exception and continue
                    continue;
                }
            }
        }
    }

    public void register(ExtensionDriver driver) {
        registerDriver(driver);
        extensions.add(driver);
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
                        return Arrays.stream((Enum<?>[]) annotation.getClass().getMethod("value").invoke(annotation));
                    } catch (NoSuchMethodException e) {
                        // Not the annotation we're looking for
                        return Arrays.stream(new Enum<?>[0]);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }).collect(Collectors.toList());

                if (attributes.size() > 0) {
                    // Build method handle
                    try {
                        m.setAccessible(true);
                        var handle = MethodHandles.lookup().unreflect(m).bindTo(driver);

                        for (var attribute : attributes) {
                            if (!handles.containsKey(attribute)) {
                                handles.put(attribute, new LinkedList<>());
                            }
                            handles.get(attribute)
                                    .add(new QueryHandle(handle, m.getAnnotation(Fallback.class), driverClass));
                        }
                    } catch (IllegalAccessException e) {
                        throw new RuntimeException("Cannot access query method: " + m.getName());
                    }
                }
            }

            driverClass = driverClass.getSuperclass();
        }

        // Reorder each stack
        handles.values().forEach(Collections::sort);
    }
}
