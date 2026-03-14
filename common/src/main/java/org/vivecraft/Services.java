package org.vivecraft;

import java.util.ServiceLoader;

public class Services {

    // In this example we provide a platform helper which provides information about what platform the mod is running on.
    // For example this can be used to check if the code is running on Forge vs Fabric, or to ask the modloader if another
    // mod is loaded.
    public static final Xplat XPLAT = load(Xplat.class);
    public static final Xevents XEVENTS = load(Xevents.class);
    public static final Xloader XLOADER = load(Xloader.class);

    // This code is used to load a service for the current environment. Your implementation of the service must be defined
    // manually by including a text file in META-INF/services named with the fully qualified class name of the service.
    // Inside the file you should write the fully qualified class name of the implementation to load for the platform. For
    // example our file on Forge points to ForgePlatformHelper while Fabric points to FabricPlatformHelper.
    public static <T> T load(Class<T> clazz) {

        final T loadedService = ServiceLoader.load(clazz)
            .findFirst()
            .orElseThrow(() -> new NullPointerException("Failed to load service for " + clazz.getName()));
        //Constants.LOG.debug("Loaded {} for service {}", loadedService, clazz);
        return loadedService;
    }
}
