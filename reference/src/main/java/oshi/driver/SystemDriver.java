package oshi.driver;

import java.util.stream.Stream;

import oshi.api.MultiSystem;
import oshi.api.PlatformEnum;
import oshi.api.hardware.disk.Disk;
import oshi.api.hardware.firmware.Firmware;
import oshi.api.hardware.nic.Nic;

@SuppressWarnings("unchecked")
public class SystemDriver implements MultiSystem {

	private Object driver;

	public SystemDriver(PlatformEnum platform) {
		switch (platform) {
		case FREEBSD:
			driver = new SystemDriverFreeBsd();
			break;
		case LINUX:
			driver = new SystemDriverLinux();
			break;
		case MACOSX:
			driver = new SystemDriverMac();
			break;
		case SOLARIS:
			driver = new SystemDriverSolaris();
			break;
		case WINDOWS:
			driver = new SystemDriverWindows();
			break;
		default:
			break;
		}
	}

	@Override
	public Stream<Nic> getNicStream() {
		try {
			return (Stream<Nic>) driver.getClass().getMethod("getNicStream").invoke(driver);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Stream<Disk> getDiskStream() {
		try {
			return (Stream<Disk>) driver.getClass().getMethod("getDiskStream").invoke(driver);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

	@Override
	public Firmware getFirmware() {
		try {
			return (Firmware) driver.getClass().getMethod("getFirmware").invoke(driver);
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}

}
