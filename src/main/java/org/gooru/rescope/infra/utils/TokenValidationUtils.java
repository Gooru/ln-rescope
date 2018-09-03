package org.gooru.rescope.infra.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author ashish
 */
public final class TokenValidationUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(TokenValidationUtils.class);

    private static final String HEADER_AUTH_PREFIX = "Token";
    private static final Pattern AUTH_PATTERN = Pattern.compile(
        '^' + HEADER_AUTH_PREFIX + "[\\s]+((?:[A-Za-z0-9+/]{4})*(?:[A-Za-z0-9+/]{2}==|[A-Za-z0-9+/]{3}=)?)\\s*$");

    public static String extractSessionToken(String authHeader) {
        if (authHeader == null || authHeader.isEmpty()) {
            LOGGER.debug("Session token is null or empty");
            return null;
        }
        Matcher authMatcher = AUTH_PATTERN.matcher(authHeader);
        if (authMatcher.matches()) {
            return authMatcher.group(1);
        }
        LOGGER.debug("Incorrect format of session token '{}'", authHeader);
        return null;
    }

    private TokenValidationUtils() {
        throw new AssertionError();
    }

}
