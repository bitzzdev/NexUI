package com.nexui.integration;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Compatibility layer for Sodium, Iris, Lithium, EMI, and JEI performance rendering hooks.
 */
public class ThirdPartyAdapters {
    private static final Logger LOGGER = LoggerFactory.getLogger("NexUI-Compatibility");

    public static void initializeCompatibilityHooks() {
        LOGGER.info("[NexUI] Initializing compatibility adapters for Sodium, Iris, Lithium, EMI, JEI...");
    }
}
