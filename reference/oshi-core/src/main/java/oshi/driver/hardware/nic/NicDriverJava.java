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
package oshi.driver.hardware.nic;

import static oshi.api.hardware.nic.internal.NicAttribute.IPV4;
import static oshi.api.hardware.nic.internal.NicAttribute.IPV6;
import static oshi.api.hardware.nic.internal.NicAttribute.MAC;
import static oshi.api.hardware.nic.internal.NicAttribute.MTU;
import static oshi.api.hardware.nic.internal.NicAttribute.NAME;
import static oshi.api.hardware.nic.internal.NicAttribute.VIRTUAL;

import java.net.NetworkInterface;
import java.net.SocketException;

import oshi.api.hardware.nic.internal.NicContainer;
import oshi.api.hardware.nic.internal.NicQuery;
import oshi.driver.ExtensionDriver;

/**
 * A driver that uses Java's built-in {@link NetworkInterface} class.
 */
public class NicDriverJava extends ExtensionDriver {

    protected NetworkInterface instance;
    private NicContainer container;

    public NicDriverJava(NicContainer container, NetworkInterface instance) {
        this.container = container;
        this.instance = instance;
    }

    @NicQuery(VIRTUAL)
    private void virtual() {
        container.virtual = instance.isVirtual();
    }

    @NicQuery(NAME)
    private void name() {
        container.name = instance.getName();
    }

    @NicQuery(MAC)
    private void mac() throws SocketException {
    }

    @NicQuery(IPV4)
    private void ipv4() {
        container.ipv4 = instance.getInterfaceAddresses().stream().map(n -> n.getAddress().getHostAddress())
                .filter(address -> !address.contains(":")).toArray(String[]::new);
    }

    @NicQuery(IPV6)
    private void ipv6() {
        container.ipv6 = instance.getInterfaceAddresses().stream().map(n -> n.getAddress().getHostAddress())
                .filter(address -> address.contains(":")).toArray(String[]::new);
    }

    @NicQuery(MTU)
    private void mtu() throws SocketException {
        container.mtu = instance.getMTU();
    }

}
