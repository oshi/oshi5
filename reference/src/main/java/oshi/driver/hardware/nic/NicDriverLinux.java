package oshi.driver.hardware.nic;

import static oshi.api.hardware.nic.internal.NicAttribute.BROADCAST;
import static oshi.api.hardware.nic.internal.NicAttribute.DEFAULT_GATEWAY;
import static oshi.api.hardware.nic.internal.NicAttribute.DESCRIPTION;
import static oshi.api.hardware.nic.internal.NicAttribute.FLAG_LOOPBACK;
import static oshi.api.hardware.nic.internal.NicAttribute.FLAG_MULTICAST;
import static oshi.api.hardware.nic.internal.NicAttribute.FLAG_RUNNING;
import static oshi.api.hardware.nic.internal.NicAttribute.FLAG_UP;
import static oshi.api.hardware.nic.internal.NicAttribute.LINK_SPEED;
import static oshi.api.hardware.nic.internal.NicAttribute.MTU;
import static oshi.api.hardware.nic.internal.NicAttribute.NETMASK;
import static oshi.api.hardware.nic.internal.NicAttribute.READ_BYTES;
import static oshi.api.hardware.nic.internal.NicAttribute.READ_DROPS;
import static oshi.api.hardware.nic.internal.NicAttribute.READ_ERRORS;
import static oshi.api.hardware.nic.internal.NicAttribute.READ_PACKETS;
import static oshi.api.hardware.nic.internal.NicAttribute.WRITE_BYTES;
import static oshi.api.hardware.nic.internal.NicAttribute.WRITE_COLLISIONS;
import static oshi.api.hardware.nic.internal.NicAttribute.WRITE_DROPS;
import static oshi.api.hardware.nic.internal.NicAttribute.WRITE_ERRORS;
import static oshi.api.hardware.nic.internal.NicAttribute.WRITE_PACKETS;

import oshi.api.hardware.nic.NicContainerLinux;
import oshi.api.hardware.nic.internal.NicQuery;
import oshi.driver.ComponentDriver;
import oshi.old.FileUtil;

public class NicDriverLinux extends ComponentDriver {

	private NicContainerLinux container;

	public NicDriverLinux(NicContainerLinux container) {
		this.container = container;
	}

	@NicQuery(READ_BYTES)
	private void readBytes() {
		container.readBytes = FileUtil
				.getUnsignedLongFromFile(String.format("/sys/class/net/%s/statistics/rx_bytes", container.name));
	}

	@NicQuery(WRITE_BYTES)
	private void writeBytes() {
		container.writeBytes = FileUtil
				.getUnsignedLongFromFile(String.format("/sys/class/net/%s/statistics/tx_bytes", container.name));
	}

	@NicQuery(READ_PACKETS)
	private void readPackets() {
		container.readPackets = FileUtil
				.getUnsignedLongFromFile(String.format("/sys/class/net/%s/statistics/rx_packets", container.name));
	}

	@NicQuery(WRITE_PACKETS)
	private void writePackets() {
		container.writePackets = FileUtil
				.getUnsignedLongFromFile(String.format("/sys/class/net/%s/statistics/tx_packets", container.name));
	}

	@NicQuery(READ_ERRORS)
	private void readErrors() {
		container.readErrors = FileUtil
				.getUnsignedLongFromFile(String.format("/sys/class/net/%s/statistics/rx_errors", container.name));
	}

	@NicQuery(WRITE_ERRORS)
	private void writeErrors() {
		container.writeErrors = FileUtil
				.getUnsignedLongFromFile(String.format("/sys/class/net/%s/statistics/tx_errors", container.name));
	}

	@NicQuery(DESCRIPTION)
	private void description() {
		// TODO Auto-generated method stub

	}

	@NicQuery(BROADCAST)
	private void broadcast() {
		// TODO Auto-generated method stub

	}

	@NicQuery(NETMASK)
	private void netmask() {
		// TODO Auto-generated method stub

	}

	@NicQuery(READ_DROPS)
	private void readDrops() {
		container.readDrops = FileUtil
				.getUnsignedLongFromFile(String.format("/sys/class/net/%s/statistics/rx_dropped", container.name));

	}

	@NicQuery(WRITE_DROPS)
	private void writeDrops() {
		container.writeDrops = FileUtil
				.getUnsignedLongFromFile(String.format("/sys/class/net/%s/statistics/tx_dropped", container.name));
	}

	@NicQuery(WRITE_COLLISIONS)
	private void writeCollisions() {
		container.writeCollisions = FileUtil
				.getUnsignedLongFromFile(String.format("/sys/class/net/%s/statistics/collisions", container.name));
	}

	@NicQuery(LINK_SPEED)
	private void linkSpeed() {
		container.linkSpeed = FileUtil
				.getUnsignedLongFromFile(String.format("/sys/class/net/%s/speed", container.name));
	}

	@NicQuery(DEFAULT_GATEWAY)
	private void defaultGateway() {
		// TODO Auto-generated method stub

	}

	@NicQuery(FLAG_UP)
	private void flagUp() {
		// TODO Auto-generated method stub

	}

	@NicQuery(FLAG_RUNNING)
	private void flagRunning() {
		// TODO Auto-generated method stub

	}

	@NicQuery(FLAG_LOOPBACK)
	private void flagLoopback() {
		// TODO Auto-generated method stub

	}

	@NicQuery(FLAG_MULTICAST)
	private void flagMulticast() {
		// TODO Auto-generated method stub

	}

	@NicQuery(MTU)
	private void mac() {
		container.mtu = FileUtil.getIntFromFile(String.format("/sys/class/net/%s/mtu", container.name));
	}

}
