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
package oshi.api;

import com.sun.jna.Platform;

import oshi.driver.SystemDriver;
import oshi.driver.SystemDriverFreeBsd;
import oshi.driver.SystemDriverLinux;
import oshi.driver.SystemDriverMac;
import oshi.driver.SystemDriverSolaris;
import oshi.driver.SystemDriverWindows;

/**
 * 
 */
public final class OSHI {

    private static final PlatformEnum PLATFORM;

    static {
        if (Platform.isWindows()) {
            PLATFORM = PlatformEnum.WINDOWS;
        } else if (Platform.isLinux()) {
            PLATFORM = PlatformEnum.LINUX;
        } else if (Platform.isMac()) {
            PLATFORM = PlatformEnum.MACOSX;
        } else if (Platform.isSolaris()) {
            PLATFORM = PlatformEnum.SOLARIS;
        } else if (Platform.isFreeBSD()) {
            PLATFORM = PlatformEnum.FREEBSD;
        } else {
            PLATFORM = PlatformEnum.UNSUPPORTED;
        }
    }

    /**
     * Get the system's platform type.
     * 
     * @return The platform type
     */
    public static PlatformEnum getPlatform() {
        return PLATFORM;
    }

    /**
     * Build a disposable handle that provides cross-platform system
     * information.
     * 
     * @return A new {@link MultiSystem} handle
     */
    public static MultiSystem getSystem() {
        switch (PLATFORM) {
        case FREEBSD:
            return new SystemDriver(new SystemDriverFreeBsd());
        case LINUX:
            return new SystemDriver(new SystemDriverLinux());
        case MACOSX:
            return new SystemDriver(new SystemDriverMac());
        case SOLARIS:
            return new SystemDriver(new SystemDriverSolaris());
        case WINDOWS:
            return new SystemDriver(new SystemDriverWindows());
        default:
            throw new UnsupportedOperationException("OSHI is not supported on this platform");
        }
    }

    /**
     * Build a disposable handle that provides Windows system information.<br>
     * <br>
     * Note: for cross-platform contexts, always use {@link #getSystem()}.
     * 
     * @return A new {@link WindowsSystem} handle
     */
    public static WindowsSystem getWindowsSystem() {
        if (PLATFORM != PlatformEnum.WINDOWS)
            throw new UnsupportedOperationException("Cannot obtain Windows handle on this platform");
        return new SystemDriverWindows();
    }

    /**
     * Build a disposable handle that provides Linux system information.<br>
     * <br>
     * Note: for cross-platform contexts, always use {@link #getSystem()}.
     * 
     * @return A new {@link LinuxSystem} handle
     */
    public static LinuxSystem getLinuxSystem() {
        if (PLATFORM != PlatformEnum.LINUX)
            throw new UnsupportedOperationException("Cannot obtain Linux handle on this platform");
        return new SystemDriverLinux();
    }

    /**
     * Build a disposable handle that provides macOS system information.<br>
     * <br>
     * Note: for cross-platform contexts, always use {@link #getSystem()}.
     * 
     * @return A new {@link MacSystem} handle
     */
    public static MacSystem getMacSystem() {
        if (PLATFORM != PlatformEnum.MACOSX)
            throw new UnsupportedOperationException("Cannot obtain MacOS handle on this platform");
        return new SystemDriverMac();
    }

    /**
     * Build a disposable handle that provides Solaris system information.<br>
     * <br>
     * Note: for cross-platform contexts, always use {@link #getSystem()}.
     * 
     * @return A new {@link SolarisSystem} handle
     */
    public static SolarisSystem getSolarisSystem() {
        if (PLATFORM != PlatformEnum.SOLARIS)
            throw new UnsupportedOperationException("Cannot obtain Solaris handle on this platform");
        return new SystemDriverSolaris();
    }

    /**
     * Build a disposable handle that provides FreeBSD system information.<br>
     * <br>
     * Note: for cross-platform contexts, always use {@link #getSystem()}.
     * 
     * @return A new {@link FreeBsdSystem} handle
     */
    public static FreeBsdSystem getFreebsdSystem() {
        if (PLATFORM != PlatformEnum.FREEBSD)
            throw new UnsupportedOperationException("Cannot obtain FreeBSD handle on this platform");
        return new SystemDriverFreeBsd();
    }

    private OSHI() {
    }
}
