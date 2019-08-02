package oshi.driver.hardware.disk;

import static oshi.api.hardware.disk.internal.DiskAttribute.CALIBRATION_RETRIES;
import static oshi.api.hardware.disk.internal.DiskAttribute.CRC_ERRORS;
import static oshi.api.hardware.disk.internal.DiskAttribute.CURRENT_PENDING_SECTOR;
import static oshi.api.hardware.disk.internal.DiskAttribute.FIRMWARE_VERSION;
import static oshi.api.hardware.disk.internal.DiskAttribute.LOAD_CYCLES;
import static oshi.api.hardware.disk.internal.DiskAttribute.MODEL_FAMILY;
import static oshi.api.hardware.disk.internal.DiskAttribute.MULTIZONE_ERROR_RATE;
import static oshi.api.hardware.disk.internal.DiskAttribute.OFFLINE_UNCORRECTABLE;
import static oshi.api.hardware.disk.internal.DiskAttribute.POWEROFF_RETRACTS;
import static oshi.api.hardware.disk.internal.DiskAttribute.POWER_CYCLES;
import static oshi.api.hardware.disk.internal.DiskAttribute.POWER_ON_TIME;
import static oshi.api.hardware.disk.internal.DiskAttribute.READ_ERROR_RATE;
import static oshi.api.hardware.disk.internal.DiskAttribute.REALLOCATED_EVENTS;
import static oshi.api.hardware.disk.internal.DiskAttribute.REALLOCATED_SECTORS;
import static oshi.api.hardware.disk.internal.DiskAttribute.ROTATION_RATE;
import static oshi.api.hardware.disk.internal.DiskAttribute.SEEK_ERROR_RATE;
import static oshi.api.hardware.disk.internal.DiskAttribute.SPIN_RETRIES;
import static oshi.api.hardware.disk.internal.DiskAttribute.SPIN_UP_TIME;
import static oshi.api.hardware.disk.internal.DiskAttribute.START_STOP_CYCLES;
import static oshi.api.hardware.disk.internal.DiskAttribute.TEMPERATURE;

import oshi.api.hardware.disk.DiskContainerLinux;
import oshi.api.hardware.disk.internal.DiskQuery;
import oshi.driver.ExtensionDriver;
import oshi.old.ExecutingCommand;

public class DiskDriverLinuxSMART extends ExtensionDriver {

	private DiskContainerLinux container;

	public DiskDriverLinuxSMART(DiskContainerLinux container) {
		this.container = container;
	}

	@DiskQuery({ MODEL_FAMILY, FIRMWARE_VERSION, ROTATION_RATE, READ_ERROR_RATE, SPIN_UP_TIME, START_STOP_CYCLES,
			REALLOCATED_SECTORS, SEEK_ERROR_RATE, POWER_ON_TIME, SPIN_RETRIES, CALIBRATION_RETRIES, POWER_CYCLES,
			POWEROFF_RETRACTS, LOAD_CYCLES, TEMPERATURE, REALLOCATED_EVENTS, CURRENT_PENDING_SECTOR,
			OFFLINE_UNCORRECTABLE, CRC_ERRORS, MULTIZONE_ERROR_RATE })
	private void diskStats() {
		// TODO parse command
		ExecutingCommand.runNative("sudo smartctl --all " + container.name);
	}
}
