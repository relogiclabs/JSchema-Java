package com.relogiclabs.jschema.internal.function;

import java.util.regex.Pattern;

public final class IPAddress {
    public static final int IPV4_VERSION = 4;
    public static final int IPV6_VERSION = 6;

    private static final String IPV4_REGEX =
        "((25[0-5]|2[0-4][0-9]|[0-1]?[0-9]?[0-9])\\.){3}" +
            "(25[0-5]|2[0-4][0-9]|[0-1]?[0-9]?[0-9])";

    private static final String IPV6_REGEX =
        "(([0-9a-fA-F]{1,4}:){7,7}[0-9a-fA-F]{1,4}|" +
            "([0-9a-fA-F]{1,4}:){1,7}:|" +
            "([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|" +
            "([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|" +
            "([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|" +
            "([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|" +
            "([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|" +
            "[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|" +
            ":((:[0-9a-fA-F]{1,4}){1,7}|:)|" +
            "fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]{1,}|" +
            "::(ffff(:0{1,4}){0,1}:){0,1}" +
            "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)|" +
            "([0-9a-fA-F]{1,4}:){1,4}:" +
            "((25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}" +
            "(25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?))";


    private static final Pattern IPV4_PATTERN = Pattern.compile(IPV4_REGEX);
    private static final Pattern IPV6_PATTERN = Pattern.compile(IPV6_REGEX);

    private IPAddress() {
        throw new UnsupportedOperationException("This class is not intended for instantiation");
    }

    public static boolean isValidIPv4(String ipAddress) {
        return IPV4_PATTERN.matcher(ipAddress).matches();
    }

    public static boolean isValidIPv6(String ipAddress) {
        return IPV6_PATTERN.matcher(ipAddress).matches();
    }
}