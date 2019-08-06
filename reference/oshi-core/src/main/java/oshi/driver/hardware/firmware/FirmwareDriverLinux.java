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
package oshi.driver.hardware.firmware;

import static oshi.api.hardware.firmware.internal.FirmwareAttribute.DESCRIPTION;
import static oshi.api.hardware.firmware.internal.FirmwareAttribute.MANUFACTURER;
import static oshi.api.hardware.firmware.internal.FirmwareAttribute.NAME;
import static oshi.api.hardware.firmware.internal.FirmwareAttribute.RELEASE_DATE;
import static oshi.api.hardware.firmware.internal.FirmwareAttribute.REVISION;
import static oshi.api.hardware.firmware.internal.FirmwareAttribute.UEFI;
import static oshi.api.hardware.firmware.internal.FirmwareAttribute.VERSION;

import oshi.api.hardware.firmware.internal.FirmwareContainerLinux;
import oshi.api.hardware.firmware.internal.FirmwareQuery;
import oshi.driver.ComponentDriver;
import oshi.driver.RequiresRoot;
import oshi.old.ExecutingCommand;
import oshi.old.FileUtil;

public class FirmwareDriverLinux extends ComponentDriver {

	private FirmwareContainerLinux container;

	public FirmwareDriverLinux(FirmwareContainerLinux container) {
		this.container = container;
	}

	private static final String SYSFS_SERIAL_PATH = "/sys/devices/virtual/dmi/id/";

	@FirmwareQuery(NAME)
	public void name() {
	}

	@FirmwareQuery(MANUFACTURER)
	private void manufacturer() {
		container.manufacturer = FileUtil.getStringFromFile(SYSFS_SERIAL_PATH + "bios_vendor").trim();
	}

	@FirmwareQuery(DESCRIPTION)
	private void description() {
		container.description = FileUtil.getStringFromFile(SYSFS_SERIAL_PATH + "modalias").trim();
	}

	@FirmwareQuery(VERSION)
	private void version() {
		container.version = FileUtil.getStringFromFile(SYSFS_SERIAL_PATH + "bios_version").trim();
	}

	@FirmwareQuery(RELEASE_DATE)
	private void releaseDate() {
		container.releaseDate = FileUtil.getStringFromFile(SYSFS_SERIAL_PATH + "bios_date").trim();
	}

	@RequiresRoot
	@FirmwareQuery(REVISION)
	private void revision() {
		final String marker = "Bios Revision:";
		for (final String checkLine : ExecutingCommand.runNative("dmidecode -t bios")) {
			if (checkLine.contains(marker)) {
				container.revision = checkLine.split(marker)[1].trim();
				return;
			}
		}
	}

	@RequiresRoot
	@FirmwareQuery(UEFI)
	private void uefi() {
		final String marker = "UEFI is supported";
		for (final String checkLine : ExecutingCommand.runNative("dmidecode -t bios")) {
			if (checkLine.contains(marker)) {
				container.uefi = true;
				return;
			}
		}

		container.uefi = false;
	}

}
