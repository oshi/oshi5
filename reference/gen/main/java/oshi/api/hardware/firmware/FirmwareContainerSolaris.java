// This file was automatically generated by the OSHI API generator; do not edit!
package oshi.api.hardware.firmware;

import oshi.driver.ComponentDriver;

/**
 * The system's firmware
 */
public class FirmwareContainerSolaris extends FirmwareContainer implements FirmwareSolaris {
  private transient ComponentDriver driver;

  public void attach(ComponentDriver driver) {
    this.driver = driver;
    super.attach(driver);
    // Query all constant attributes:
  }
}
