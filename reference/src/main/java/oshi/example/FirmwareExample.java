package oshi.example;

import oshi.api.OSHI;

public class FirmwareExample {

	public static void main(String[] args) {

		var firmware = OSHI.getSystem().getFirmware();
		System.out.println("=== CrossPlatform Firmware ===");
		System.out.println("Name: " + firmware.getName());
		System.out.println("Description: " + firmware.getDescription());
		System.out.println("Version: " + firmware.getVersion());
		System.out.println("Revision: " + firmware.getRevision());
		System.out.println("Release: " + firmware.getReleaseDate());

		var linuxFirmware = OSHI.getLinuxSystem().getFirmware();
		System.out.println("=== Linux Firmware ===");
		System.out.println("UEFI support: " + linuxFirmware.getUefi());
	}
}
