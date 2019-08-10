/**
 * OSHI (https://github.com/oshi/oshi)
 *
 * Copyright (c) 2010 - 2019 The OSHI Project Team:
 * https://github.com/oshi/oshi/graphs/contributors
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package oshi.driver.hardware.disk;

import static oshi.api.hardware.disk.internal.DiskAttributeEnum.CALIBRATION_RETRIES;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.CRC_ERRORS;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.CURRENT_PENDING_SECTOR;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.FIRMWARE_VERSION;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.LOAD_CYCLES;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.MODEL_FAMILY;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.MULTIZONE_ERROR_RATE;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.OFFLINE_UNCORRECTABLE;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.POWEROFF_RETRACTS;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.POWER_CYCLES;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.POWER_ON_TIME;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.READ_ERROR_RATE;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.REALLOCATED_EVENTS;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.REALLOCATED_SECTORS;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.ROTATION_RATE;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.SEEK_ERROR_RATE;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.SPIN_RETRIES;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.SPIN_UP_TIME;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.START_STOP_CYCLES;
import static oshi.api.hardware.disk.internal.DiskAttributeEnum.TEMPERATURE;

import java.io.IOException;

import oshi.api.hardware.disk.internal.DiskContainerLinux;
import oshi.api.hardware.disk.internal.DiskQuery;
import oshi.driver.ExtensionDriver;
import oshi.driver.annotation.RequiresRoot;
import oshi.old.ExecutingCommand;

public class DiskDriverLinuxSMART extends ExtensionDriver {

    private DiskContainerLinux container;

    @Override
    public boolean compatible() {
        try {
            return 127 != Runtime.getRuntime().exec("smartctl").waitFor();
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return false;
    }

    @RequiresRoot
    @DiskQuery({ MODEL_FAMILY, FIRMWARE_VERSION, ROTATION_RATE, READ_ERROR_RATE, SPIN_UP_TIME, START_STOP_CYCLES,
            REALLOCATED_SECTORS, SEEK_ERROR_RATE, POWER_ON_TIME, SPIN_RETRIES, CALIBRATION_RETRIES, POWER_CYCLES,
            POWEROFF_RETRACTS, LOAD_CYCLES, TEMPERATURE, REALLOCATED_EVENTS, CURRENT_PENDING_SECTOR,
            OFFLINE_UNCORRECTABLE, CRC_ERRORS, MULTIZONE_ERROR_RATE })
    private void diskStats() throws IOException {
        var output = ExecutingCommand.runNative("sudo smartctl --attributes " + container.path);
        for (int i = 7; i < 24; i++) {
            String[] line = output.get(i).trim().split("\\s+");
            switch (Integer.parseInt(line[0])) {
            case 1:
                container.readErrorRate = Long.parseLong(line[9]);
                break;
            case 3:
                container.spinUpTime = Long.parseLong(line[9]);
                break;
            case 4:
                container.startStopCycles = Long.parseLong(line[9]);
                break;
            case 5:
                container.reallocatedSectors = Long.parseLong(line[9]);
                break;
            case 7:
                container.seekErrorRate = Long.parseLong(line[9]);
                break;
            case 9:
                container.powerOnTime = Long.parseLong(line[9]);
                break;
            case 10:
                container.spinRetries = Long.parseLong(line[9]);
                break;
            case 11:
                container.calibrationRetries = Long.parseLong(line[9]);
                break;
            case 12:
                container.powerCycles = Long.parseLong(line[9]);
                break;
            case 192:
                container.poweroffRetracts = Long.parseLong(line[9]);
                break;
            case 193:
                container.loadCycles = Long.parseLong(line[9]);
                break;
            case 194:
                container.temperature = Long.parseLong(line[9]);
                break;
            case 196:
                container.reallocatedEvents = Long.parseLong(line[9]);
                break;
            case 197:
                container.currentPendingSector = Long.parseLong(line[9]);
                break;
            case 198:
                container.offlineUncorrectable = Long.parseLong(line[9]);
                break;
            case 199:
                container.crcErrors = Long.parseLong(line[9]);
                break;
            case 200:
                container.multizoneErrorRate = Long.parseLong(line[9]);
                break;
            }
        }
    }
}
