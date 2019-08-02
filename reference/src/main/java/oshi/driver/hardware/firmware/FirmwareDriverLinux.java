package oshi.driver.hardware.firmware;

import static oshi.api.hardware.firmware.internal.FirmwareAttribute.DESCRIPTION;
import static oshi.api.hardware.firmware.internal.FirmwareAttribute.MANUFACTURER;
import static oshi.api.hardware.firmware.internal.FirmwareAttribute.NAME;
import static oshi.api.hardware.firmware.internal.FirmwareAttribute.RELEASE_DATE;
import static oshi.api.hardware.firmware.internal.FirmwareAttribute.REVISION;
import static oshi.api.hardware.firmware.internal.FirmwareAttribute.UEFI;
import static oshi.api.hardware.firmware.internal.FirmwareAttribute.VERSION;

import oshi.api.hardware.firmware.FirmwareContainerLinux;
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
