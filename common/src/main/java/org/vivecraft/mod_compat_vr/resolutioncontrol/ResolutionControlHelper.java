package org.vivecraft.mod_compat_vr.resolutioncontrol;

import io.github.ultimateboomer.resolutioncontrol.ResolutionControlMod;
import org.vivecraft.Services;
import org.vivecraft.Xloader;

public class ResolutionControlHelper {

    public static boolean isLoaded() {
        return Services.XLOADER.isModLoaded("resolutioncontrol");
    }

    /**
     * @return current render scale
     */
    public static float getCurrentScaleFactor() {
        //TODO return (float) ResolutionControlMod.getInstance().getCurrentScaleFactor();
        return 0;
    }
}
