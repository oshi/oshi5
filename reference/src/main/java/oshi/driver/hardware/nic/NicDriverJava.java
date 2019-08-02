package oshi.driver.hardware.nic;

import static oshi.api.hardware.nic.internal.NicAttribute.IPV4;
import static oshi.api.hardware.nic.internal.NicAttribute.IPV6;
import static oshi.api.hardware.nic.internal.NicAttribute.MAC;
import static oshi.api.hardware.nic.internal.NicAttribute.MTU;
import static oshi.api.hardware.nic.internal.NicAttribute.NAME;
import static oshi.api.hardware.nic.internal.NicAttribute.VIRTUAL;

import java.net.NetworkInterface;
import java.net.SocketException;

import oshi.api.hardware.nic.NicContainer;
import oshi.api.hardware.nic.internal.NicQuery;
import oshi.driver.ExtensionDriver;

/**
 * A driver that uses Java's built-in {@link NetworkInterface} class.
 */
public class NicDriverJava extends ExtensionDriver {

	protected NetworkInterface instance;
	private NicContainer container;

	public NicDriverJava(NicContainer container, NetworkInterface instance) {
		this.container = container;
		this.instance = instance;
	}

	@NicQuery(VIRTUAL)
	private void virtual() {
		container.virtual = instance.isVirtual();
	}

	@NicQuery(NAME)
	private void name() {
		container.name = instance.getName();
	}

	@NicQuery(MAC)
	private void mac() throws SocketException {
	}

	@NicQuery(IPV4)
	private void ipv4() {
		container.ipv4 = instance.getInterfaceAddresses().stream().map(n -> n.getAddress().getHostAddress())
				.filter(address -> !address.contains(":")).toArray(String[]::new);
	}

	@NicQuery(IPV6)
	private void ipv6() {
		container.ipv6 = instance.getInterfaceAddresses().stream().map(n -> n.getAddress().getHostAddress())
				.filter(address -> address.contains(":")).toArray(String[]::new);
	}

	@NicQuery(MTU)
	private void mtu() throws SocketException {
		container.mtu = instance.getMTU();
	}

}
