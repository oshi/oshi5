package oshi.driver;

import java.util.stream.Stream;

import oshi.api.WindowsSystem;
import oshi.api.hardware.disk.DiskWindows;
import oshi.api.hardware.firmware.FirmwareWindows;
import oshi.api.hardware.nic.NicWindows;

public class SystemDriverWindows implements WindowsSystem {

	@Override
	public Stream<NicWindows> getNicStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<DiskWindows> getDiskStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FirmwareWindows getFirmware() {
		// TODO Auto-generated method stub
		return null;
	}

}
