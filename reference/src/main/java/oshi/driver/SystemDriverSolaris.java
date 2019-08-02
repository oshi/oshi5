package oshi.driver;

import java.util.stream.Stream;

import oshi.api.SolarisSystem;
import oshi.api.hardware.disk.DiskSolaris;
import oshi.api.hardware.firmware.FirmwareSolaris;
import oshi.api.hardware.nic.NicSolaris;

public class SystemDriverSolaris implements SolarisSystem {

	@Override
	public Stream<NicSolaris> getNicStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<DiskSolaris> getDiskStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FirmwareSolaris getFirmware() {
		// TODO Auto-generated method stub
		return null;
	}

}
