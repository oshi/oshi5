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

import static oshi.api.hardware.disk.DiskAttribute.POWER_CYCLES;
import static oshi.api.hardware.disk.DiskAttribute.POWER_ON_TIME;
import static oshi.api.hardware.disk.DiskAttribute.READ_BYTES;
import static oshi.api.hardware.disk.DiskAttribute.SIZE;
import static oshi.api.hardware.disk.DiskAttribute.TEMPERATURE;
import static oshi.api.hardware.disk.DiskAttribute.WRITE_BYTES;

import oshi.api.OSHI;

public class DiskExample {
    public static void main(String[] args) {

        example1();
        example2();
        example3();
        example4();
    }

    private static void example1() {
        System.out.println("Cross-platform access that makes one query");

        OSHI.getSystem().getDiskStream().forEach(disk -> {
            disk.query(SIZE, READ_BYTES, WRITE_BYTES);

            System.out.println("Disk name: " + disk.getName());
            System.out.println("\tSize: " + disk.getSize());
            System.out.println("\tRead bytes: " + disk.getReadBytes());
            System.out.println("\tWrite bytes: " + disk.getWriteBytes());
        });

        System.out.println();
    }

    private static void example2() {
        System.out.println("Platform-specific access that makes one query");

        OSHI.getLinuxSystem().getDiskStream().forEach(disk -> {
            disk.query(SIZE, READ_BYTES, WRITE_BYTES);

            System.out.println("Disk name: " + disk.getName());
            System.out.println("\tSize: " + disk.getSize());
            System.out.println("\tRead bytes: " + disk.getReadBytes());
            System.out.println("\tWrite bytes: " + disk.getWriteBytes());
        });

        System.out.println();
    }

    private static void example3() {
        System.out.println("Cross-platform access that makes three queries");

        OSHI.getSystem().getDiskStream().forEach(disk -> {
            System.out.println("Disk name: " + disk.getName());
            System.out.println("\tSize: " + disk.querySize());
            System.out.println("\tRead bytes: " + disk.queryReadBytes());
            System.out.println("\tWrite bytes: " + disk.queryWriteBytes());
        });

        System.out.println();
    }

    private static void example4() {
        System.out.println("Cross-platform access for SMART attributes");

        OSHI.getSystem().getDiskStream().forEach(disk -> {
            disk.query(POWER_CYCLES, POWER_ON_TIME, TEMPERATURE);

            System.out.println("Disk name: " + disk.getName());
            System.out.println("\tPower cycles: " + disk.getPowerCycles());
            System.out.println("\tPower on hours: " + disk.getPowerOnTime());
            System.out.println("\tTemperature: " + disk.getTemperature());
        });

        System.out.println();
    }
}
