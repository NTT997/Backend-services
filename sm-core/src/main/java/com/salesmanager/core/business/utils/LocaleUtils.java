package com.salesmanager.core.business.utils;

import java.util.Locale;

public class LocaleUtils {

    public static final Locale VIETNAM_VI = new Locale("vi", "VN");
    public static final Locale VIETNAM_EN = new Locale("en", "VN");

    public static Locale getLocaleFromStore(String languageCode, String countryCode) {
        if ("VN".equalsIgnoreCase(countryCode)) {
            if ("en".equalsIgnoreCase(languageCode)) {
                return VIETNAM_EN; // English in Vietnam
            }
            return VIETNAM_VI;     // Default: Vietnamese in Vietnam
        }

        // Fallback: try given codes, default to US
        try {
            return new Locale(
                languageCode != null ? languageCode : "en",
                countryCode != null ? countryCode : "US"
            );
        } catch (Exception e) {
            return Locale.US;
        }
    }
}
