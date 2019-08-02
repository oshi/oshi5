package oshi.api;

import com.sun.jna.Platform;

import oshi.driver.SystemDriver;
import oshi.driver.SystemDriverFreeBsd;
import oshi.driver.SystemDriverLinux;
import oshi.driver.SystemDriverMac;
import oshi.driver.SystemDriverSolaris;
import oshi.driver.SystemDriverWindows;

public final class OSHI {

	private static final PlatformEnum platform;

	static {
		if (Platform.isWindows()) {
			platform = PlatformEnum.WINDOWS;
		} else if (Platform.isLinux()) {
			platform = PlatformEnum.LINUX;
		} else if (Platform.isMac()) {
			platform = PlatformEnum.MACOSX;
		} else if (Platform.isSolaris()) {
			platform = PlatformEnum.SOLARIS;
		} else if (Platform.isFreeBSD()) {
			platform = PlatformEnum.FREEBSD;
		} else {
			platform = PlatformEnum.UNKNOWN;
			throw new RuntimeException("Unsupported platform");
		}
	}

	/**
	 * Get the system's platform type.
	 * 
	 * @return The platform type
	 */
	public static PlatformEnum getPlatform() {
		return platform;
	}

	public static MultiSystem getSystem() {
		if (platform == PlatformEnum.UNKNOWN)
			throw new UnsupportedOperationException();
		return new SystemDriver(platform);
	}

	public static WindowsSystem getWindowsSystem() {
		if (platform != PlatformEnum.WINDOWS)
			throw new UnsupportedOperationException();
		return new SystemDriverWindows();
	}

	public static LinuxSystem getLinuxSystem() {
		if (platform != PlatformEnum.LINUX)
			throw new UnsupportedOperationException();
		return new SystemDriverLinux();
	}

	public static MacSystem getMacSystem() {
		if (platform != PlatformEnum.MACOSX)
			throw new UnsupportedOperationException();
		return new SystemDriverMac();
	}

	public static SolarisSystem getSolarisSystem() {
		if (platform != PlatformEnum.SOLARIS)
			throw new UnsupportedOperationException();
		return new SystemDriverSolaris();
	}

	public static FreeBsdSystem getFreebsdSystem() {
		if (platform != PlatformEnum.FREEBSD)
			throw new UnsupportedOperationException();
		return new SystemDriverFreeBsd();
	}

}
