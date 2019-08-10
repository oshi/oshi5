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
package oshi.example;

import static oshi.api.hardware.nic.NicAttribute.IPV4;
import static oshi.api.hardware.nic.NicAttribute.LINK_SPEED;
import static oshi.api.hardware.nic.NicAttribute.MTU;
import static oshi.api.hardware.nic.NicAttribute.READ_BYTES;
import static oshi.api.hardware.nic.NicAttribute.WRITE_BYTES;

import java.util.Arrays;

import oshi.api.OSHI;

public class NicExample {

    public static void main(String[] args) throws Exception {
        example1();
    }

    private static void example1() {
        System.out.println("Cross-platform usage that makes one query");

        OSHI.getSystem().getNicStream().forEach(nic -> {
            nic.query(READ_BYTES, WRITE_BYTES, MTU, IPV4, LINK_SPEED);

            System.out.println("NIC: " + nic.getName());
            System.out.println("\tReadBytes: " + nic.getReadBytes());
            System.out.println("\tWriteBytes: " + nic.getWriteBytes());
            System.out.println("\tMTU: " + nic.getMtu());
            System.out.println("\tIPv4: " + Arrays.toString(nic.getIpv4()));
            System.out.println("\tLink Speed: " + nic.getLinkSpeed());
        });

        System.out.println();
    }

}
