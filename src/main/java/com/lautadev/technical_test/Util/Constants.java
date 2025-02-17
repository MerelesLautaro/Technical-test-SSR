package com.lautadev.technical_test.Util;

import java.util.Set;

public class Constants {

    private Constants() {
    }

    public static final Set<String> UNPROTECTED_PATHS = Set.of(
            "api/v1/authentication/users",
            "swagger-ui.html",
            "swagger-ui/.*",
            "v3/api-docs",
            "swagger.json/.*");
}
