package oshi.driver;

import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import oshi.api.LinuxSystem;
import oshi.api.hardware.disk.DiskContainerLinux;
import oshi.api.hardware.disk.DiskLinux;
import oshi.api.hardware.firmware.FirmwareContainerLinux;
import oshi.api.hardware.firmware.FirmwareLinux;
import oshi.api.hardware.nic.NicContainerLinux;
import oshi.api.hardware.nic.NicLinux;
import oshi.driver.hardware.disk.DiskDriverLinux;
import oshi.driver.hardware.firmware.FirmwareDriverLinux;
import oshi.driver.hardware.nic.NicDriverJava;
import oshi.driver.hardware.nic.NicDriverLinux;
import oshi.old.Udev;

public class SystemDriverLinux implements LinuxSystem {

	@Override
	public Stream<NicLinux> getNicStream() {
		try {
			return NetworkInterface.networkInterfaces().map(nif -> {
				var container = new NicContainerLinux();
				container.name = nif.getName();
				var driver = new NicDriverLinux(container);
				driver.register(new NicDriverJava(container, nif));

				container.attach(driver);
				return container;
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
			device = Udev.INSTANCE.udev_device_new_from_syspath(handle, Udev.INSTANCE.udev_list_entry_get_name(entry));
			if (device == null) {
				break;
			}

			// Ignore loopback and ram disks; do nothing
			if (!Udev.INSTANCE.udev_device_get_devnode(device).startsWith("/dev/loop")
					&& !Udev.INSTANCE.udev_device_get_devnode(device).startsWith("/dev/ram")) {
				if ("disk".equals(Udev.INSTANCE.udev_device_get_devtype(device))) {
					var container = new DiskContainerLinux();
					container.name = Udev.INSTANCE.udev_list_entry_get_name(entry);
					var driver = new DiskDriverLinux(container);
					// TODO SMART extension
					container.attach(driver);

					results.add(container);
				}
			}

			entry = Udev.INSTANCE.udev_list_entry_get_next(oldEntry);
			Udev.INSTANCE.udev_device_unref(device);
		}

		return results.stream();
	}

	@Override
	public FirmwareLinux getFirmware() {
		var container = new FirmwareContainerLinux();
		var driver = new FirmwareDriverLinux(container);

		container.attach(driver);
		return container;
	}

}
