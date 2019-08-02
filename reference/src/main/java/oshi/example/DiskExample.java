package oshi.example;

import oshi.api.OSHI;

public class DiskExample {
	public static void main(String[] args) {

		OSHI.getLinuxSystem().getDiskStream().forEach(disk -> {
			System.out.println("Disk name: " + disk.getName());
			System.out.println("\tSize: " + disk.querySize());
			System.out.println("\tRead bytes: " + disk.queryReadBytes());
			System.out.println("\tWrite bytes: " + disk.queryWriteBytes());
			System.out.println("\tQueue length: " + disk.queryQueueLength());
			System.out.println("\tTransfer time: " + disk.queryTransferTime());
		});
	}
}
