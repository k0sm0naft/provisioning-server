package com.voxloud.provisioning.utils;

public class MacAddressUtils {
    private static final String NORMALIZED_MAC_REGEX = "([a-f0-9]{2}-){5}[a-f0-9]{2}";
    private static final String MAC_CLEANUP_REGEX = "[:]";
    private static final String INSERT_HYPHENS_REGEX = "(.{2})(?!$)";
    private static final String INSERT_HYPHEN_REPLACEMENT = "$1-";
    private static final String EMPTY_STRING = "";

    private MacAddressUtils() {
        throw new UnsupportedOperationException("Utility class cannot be instantiated");
    }

    public static String normalize(String macAddress) {
        macAddress = macAddress.toLowerCase();

        if (isNormalized(macAddress)) {
            return macAddress;
        }

        String cleanedMac = macAddress.replaceAll(MAC_CLEANUP_REGEX, EMPTY_STRING);
        return cleanedMac.replaceAll(INSERT_HYPHENS_REGEX, INSERT_HYPHEN_REPLACEMENT);
    }

    private static boolean isNormalized(String macAddress) {
        return macAddress.matches(NORMALIZED_MAC_REGEX);
    }
}
