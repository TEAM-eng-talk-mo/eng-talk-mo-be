package com.engtalkmo.global.util;

import com.engtalkmo.global.error.exception.BusinessException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

import java.util.Base64;

import static com.engtalkmo.global.error.exception.ErrorCode.INTERNAL_SERVER_ERROR;

@Slf4j
public class CookieUtil {

    private static final ObjectMapper objectMapper = new ObjectMapper();

    public static void addCookie(HttpServletResponse response, String name, String value, int maxAge) {
        Cookie cookie = new Cookie(name, value);
        cookie.setPath("/");
        cookie.setMaxAge(maxAge);
        response.addCookie(cookie);
    }

    public static void deleteCookie(HttpServletRequest request, HttpServletResponse response, String name) {
        Cookie[] cookies = request.getCookies();

        if (cookies == null) {
            return;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                cookie.setValue("");
                cookie.setPath("/");
                cookie.setMaxAge(0);
                response.addCookie(cookie);
            }
        }
    }

    public static String serialize(Object obj) {
        try {
            return Base64.getUrlEncoder()
                    .encodeToString(objectMapper.writeValueAsString(obj).getBytes());
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(INTERNAL_SERVER_ERROR);
        }
    }

    public static <T> T deserialize(Cookie cookie, Class<T> cls) {
        try {
            return objectMapper.readValue(
                    Base64.getUrlDecoder().decode(cookie.getValue()), cls);
        } catch (Exception e) {
            log.error(e.getMessage(), e);
            throw new BusinessException(INTERNAL_SERVER_ERROR);
        }
    }
}
