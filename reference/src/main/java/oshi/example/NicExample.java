package oshi.example;

import java.util.Arrays;

import oshi.api.OSHI;

public class NicExample {

	public static void main(String[] args) throws Exception {

		OSHI.getLinuxSystem().getNicStream().forEach(nic -> {
			System.out.println("NIC: " + nic.getName());
			System.out.println("\tReadBytes: " + nic.queryReadBytes());
			System.out.println("\tWriteBytes: " + nic.queryWriteBytes());
			System.out.println("\tMTU: " + nic.queryMtu());
			System.out.println("\tIPv4: " + Arrays.toString(nic.queryIpv4()));
			System.out.println("\tLink Speed: " + nic.queryLinkSpeed());
		});

	}

}
