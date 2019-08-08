package com.serverless.handler.util;

import com.google.common.base.Strings;

public class HandlerUtils {
    public static boolean isBlank(String str) {
        return Strings.nullToEmpty(str).trim().isEmpty();
    }
}
