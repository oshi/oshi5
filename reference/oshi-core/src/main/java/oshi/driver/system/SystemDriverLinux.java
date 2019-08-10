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
package oshi.driver.system;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import oshi.api.LinuxSystem;
import oshi.api.hardware.disk.DiskLinux;
import oshi.api.hardware.disk.internal.DiskContainerLinux;
import oshi.api.hardware.firmware.FirmwareLinux;
import oshi.api.hardware.firmware.internal.FirmwareContainerLinux;
import oshi.api.hardware.nic.NicLinux;
import oshi.api.hardware.nic.internal.NicContainerLinux;
import oshi.driver.ContainerFactory;
import oshi.driver.hardware.disk.DiskDriverLinux;
import oshi.driver.hardware.disk.DiskDriverLinuxSMART;
import oshi.driver.hardware.firmware.FirmwareDriverLinux;
import oshi.driver.hardware.nic.NicDriverJava;
import oshi.driver.hardware.nic.NicDriverLinux;
import oshi.old.GlobalConfig;
import oshi.old.Udev;

public class SystemDriverLinux implements LinuxSystem {

    @Override
    public Stream<NicLinux> getNicStream() {
        try {
            return NetworkInterface.networkInterfaces().map(nif -> {
                return ContainerFactory.build(NicContainerLinux.class, container -> {
                    container.name = nif.getName();
                }, NicDriverLinux.class, extensions -> {
                    if (GlobalConfig.get("oshi.driver.extension.networkinterface", true))
                        extensions.add(new NicDriverJava(nif));
                });
            });
        } catch (SocketException e) {
            // No interfaces found or I/O error occurred
            return Stream.empty();
        }
    }

    @Override
    public Stream<DiskLinux> getDiskStream() {
        List<DiskLinux> results = new ArrayList<>();

        Udev.UdevDevice device = null;
        Udev.UdevListEntry entry;
        Udev.UdevListEntry oldEntry;

        Udev.UdevHandle handle = Udev.INSTANCE.udev_new();
        Udev.UdevEnumerate enumerate = Udev.INSTANCE.udev_enumerate_new(handle);
        Udev.INSTANCE.udev_enumerate_add_match_subsystem(enumerate, "block");
        Udev.INSTANCE.udev_enumerate_scan_devices(enumerate);

        entry = Udev.INSTANCE.udev_enumerate_get_list_entry(enumerate);
        while (true) {
            oldEntry = entry;
            String name = Udev.INSTANCE.udev_list_entry_get_name(entry);
            device = Udev.INSTANCE.udev_device_new_from_syspath(handle, name);
            if (device == null) {
                break;
            }

            String path = Udev.INSTANCE.udev_device_get_devnode(device);

            // Ignore loopback and ram disks; do nothing
            if (!path.startsWith("/dev/loop") && !path.startsWith("/dev/ram")) {
                if ("disk".equals(Udev.INSTANCE.udev_device_get_devtype(device))) {
                    var c = ContainerFactory.build(DiskContainerLinux.class, container -> {
                        container.name = name;
                        container.path = path;
                    }, DiskDriverLinux.class, extensions -> {
                        if (GlobalConfig.get("oshi.driver.extension.smart", true))
                            extensions.add(new DiskDriverLinuxSMART());
                    });

                    results.add(c);
                }
            }

            entry = Udev.INSTANCE.udev_list_entry_get_next(oldEntry);
            Udev.INSTANCE.udev_device_unref(device);
        }

        return results.stream();
    }

    @Override
    public FirmwareLinux getFirmware() {
        return ContainerFactory.build(FirmwareContainerLinux.class, FirmwareDriverLinux.class);
    }

}
