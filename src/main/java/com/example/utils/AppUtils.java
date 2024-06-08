package com.example.utils;

import com.example.models.UserAccount;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class AppUtils {

    public static final String ATT_NAME_CONNECTION =
            "ATTRIBUTE_FOR_CONNECTION";
    private static final String ATT_NAME_USER_NAME =
            "ATTRIBUTE_FOR_STORE_USER_NAME_IN_COOKIE";

    // Store Connection in request attribute.
    // (Information stored only exist during requests)
    public static void storeConnection(ServletRequest request,
                                       Connection conn) {
        request.setAttribute(ATT_NAME_CONNECTION, conn);
    }

    public static Connection getStoredConnection(ServletRequest request) {
        try {
            Class.forName("org.h2.Driver");
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
        try {
            return DriverManager.getConnection("jdbc:h2:mem:default");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    // Store user info in Session.
    public static void storeLoginedUser(HttpSession session,
                                        UserAccount loginedUser) {
        // On the JSP can access ${loginedUser}
        session.setAttribute("loginedUser", loginedUser);
    }

    // Get the user information stored in the session.
    public static UserAccount getLoginedUser(HttpSession session) {
        UserAccount loginedUser = (UserAccount)
                session.getAttribute("loginedUser");
        return loginedUser;
    }

    // Store info in Cookie
    public static void storeUserCookie(HttpServletResponse response,
                                       UserAccount user) {
        System.out.println("Store user cookie");
        Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME,
                user.getUserName());

        // 1 day (Convert to seconds)
        cookieUserName.setMaxAge(24 * 60 * 60);
        response.addCookie(cookieUserName);
    }

    public static String getUserNameInCookie(HttpServletRequest
                                                     request) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (ATT_NAME_USER_NAME.equals(cookie.getName())) {
                    return cookie.getValue();
                }
            }
        }
        return null;
    }

    // Delete cookie.
    public static void deleteUserCookie(HttpServletResponse response) {
        Cookie cookieUserName = new Cookie(ATT_NAME_USER_NAME,
                null);
        // 0 seconds (Expires immediately)
        cookieUserName.setMaxAge(0);
        response.addCookie(cookieUserName);
    }
}

