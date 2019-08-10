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

import oshi.api.OSHI;
import oshi.api.hardware.firmware.Firmware;
import oshi.api.hardware.firmware.FirmwareLinux;

public class FirmwareExample {

    public static void main(String[] args) {
        example1();
        example2();
    }

    private static void example1() {
        System.out.println("Cross-platform access");

        Firmware firmware = OSHI.getSystem().getFirmware();
        System.out.println("Name: " + firmware.getName());
        System.out.println("Description: " + firmware.getDescription());
        System.out.println("Version: " + firmware.getVersion());
        System.out.println("Revision: " + firmware.getRevision());
        System.out.println("Release: " + firmware.getReleaseDate());

        System.out.println();
    }

    private static void example2() {
        System.out.println("Platform-specific access");

        FirmwareLinux firmware = OSHI.getLinuxSystem().getFirmware();
        System.out.println("UEFI support: " + firmware.getUefi());

        System.out.println();
    }
}
