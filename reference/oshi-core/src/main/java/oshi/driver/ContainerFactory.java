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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import oshi.api.internal.AbstractContainer;

public class ContainerFactory {

    public static <C extends AbstractContainer, D extends ComponentDriver> C build(Class<C> containerClass,
            Consumer<C> containerConfigurator, Class<D> driverClass,
            Consumer<List<ExtensionDriver>> driverConfigurator) {

        try {
            // Build and configure container
            C container = containerClass.getConstructor().newInstance();
            containerConfigurator.accept(container);

            // Build driver
            D driver = driverClass.getConstructor().newInstance();
            var driverContainerField = driverClass.getDeclaredField("container");
            driverContainerField.setAccessible(true);
            driverContainerField.set(driver, container);

            // Configure extensions
            ArrayList<ExtensionDriver> extensions = new ArrayList<>();
            driverConfigurator.accept(extensions);
            extensions.trimToSize();
            for (var extension : extensions) {
                var extensionContainerField = extension.getClass().getDeclaredField("container");
                extensionContainerField.setAccessible(true);
                extensionContainerField.set(extension, container);
            }

            driver.initialize(Collections.unmodifiableList(extensions));
            container.attach(driver);
            return container;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    public static <C extends AbstractContainer, D extends ComponentDriver> C build(Class<C> containerClass,
            Class<D> driverClass) {
        return build(containerClass, container -> {
            // Do nothing
        }, driverClass, extensions -> {
            // Do nothing
        });
    }

}
