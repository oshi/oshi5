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
package oshi.driver.hardware.disk;

import static oshi.api.hardware.disk.internal.DiskAttribute.MODEL;
import static oshi.api.hardware.disk.internal.DiskAttribute.NAME;
import static oshi.api.hardware.disk.internal.DiskAttribute.QUEUE_LENGTH;
import static oshi.api.hardware.disk.internal.DiskAttribute.READS;
import static oshi.api.hardware.disk.internal.DiskAttribute.READ_BYTES;
import static oshi.api.hardware.disk.internal.DiskAttribute.SERIAL;
import static oshi.api.hardware.disk.internal.DiskAttribute.SIZE;
import static oshi.api.hardware.disk.internal.DiskAttribute.TRANSFER_TIME;
import static oshi.api.hardware.disk.internal.DiskAttribute.WRITES;
import static oshi.api.hardware.disk.internal.DiskAttribute.WRITE_BYTES;

import oshi.api.hardware.disk.internal.DiskContainerLinux;
import oshi.api.hardware.disk.internal.DiskQuery;
import oshi.driver.ComponentDriver;
import oshi.old.ParseUtil;
import oshi.old.Udev;

public class DiskDriverLinux extends ComponentDriver {

	private DiskContainerLinux container;

	public DiskDriverLinux(DiskContainerLinux container) {
		this.container = container;
	}

	/**
	 * The size of a logical sector in bytes.
	 */
	private static final int SECTOR_SIZE = 512;

	// Get a list of orders to pass to ParseUtil
	private static final int[] UDEV_STAT_ORDERS = new int[] {
			//
			0, // Reads
			2, // Read bytes
			4, // Writes
			6, // Write bytes
			8, // Queue length
			9 // Transfer time
	};

	// There are at least 11 elements in udev stat output. Some platforms have
	// 12 but we want the last 11. ParseUtil works from the right
	private static final int UDEV_STAT_LENGTH = 11;

	@DiskQuery({ MODEL, SERIAL, SIZE, NAME })
	private void diskInfo() {
		Udev.UdevHandle handle = Udev.INSTANCE.udev_new();
		Udev.UdevDevice device = Udev.INSTANCE.udev_device_new_from_syspath(handle, container.name);

		container.model = Udev.INSTANCE.udev_device_get_property_value(device, "ID_MODEL");
		container.serial = Udev.INSTANCE.udev_device_get_property_value(device, "ID_SERIAL_SHORT");
		container.size = ParseUtil.parseLongOrDefault(Udev.INSTANCE.udev_device_get_sysattr_value(device, "size"), 0L)
				* 512;

		Udev.INSTANCE.udev_device_unref(device);
		Udev.INSTANCE.udev_unref(handle);
	}

	@DiskQuery({ READS, READ_BYTES, WRITES, WRITE_BYTES, QUEUE_LENGTH, TRANSFER_TIME })
	private void diskStats() {
		Udev.UdevHandle handle = Udev.INSTANCE.udev_new();
		Udev.UdevDevice device = Udev.INSTANCE.udev_device_new_from_syspath(handle, container.name);

		long[] deviceStats = ParseUtil.parseStringToLongArray(
				Udev.INSTANCE.udev_device_get_sysattr_value(device, "stat"), UDEV_STAT_ORDERS, UDEV_STAT_LENGTH, ' ');

		container.reads = deviceStats[0];
		container.readBytes = deviceStats[1] * SECTOR_SIZE;
		container.writes = deviceStats[2];
		container.writeBytes = deviceStats[3] * SECTOR_SIZE;
		container.queueLength = deviceStats[4];
		container.transferTime = deviceStats[5];

		Udev.INSTANCE.udev_device_unref(device);
		Udev.INSTANCE.udev_unref(handle);
	}

}
