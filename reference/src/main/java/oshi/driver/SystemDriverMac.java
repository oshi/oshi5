package oshi.driver;

import java.util.stream.Stream;

import oshi.api.MacSystem;
import oshi.api.hardware.disk.DiskMac;
import oshi.api.hardware.firmware.FirmwareMac;
import oshi.api.hardware.nic.NicMac;

public class SystemDriverMac implements MacSystem {

	@Override
	public Stream<NicMac> getNicStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<DiskMac> getDiskStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FirmwareMac getFirmware() {
		// TODO Auto-generated method stub
		return null;
	}

}
