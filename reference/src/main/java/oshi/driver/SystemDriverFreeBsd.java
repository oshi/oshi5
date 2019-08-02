package oshi.driver;

import java.util.stream.Stream;

import oshi.api.FreeBsdSystem;
import oshi.api.hardware.disk.DiskFreeBsd;
import oshi.api.hardware.firmware.FirmwareFreeBsd;
import oshi.api.hardware.nic.NicFreeBsd;

public class SystemDriverFreeBsd implements FreeBsdSystem {

	@Override
	public Stream<NicFreeBsd> getNicStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Stream<DiskFreeBsd> getDiskStream() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public FirmwareFreeBsd getFirmware() {
		// TODO Auto-generated method stub
		return null;
	}

}
