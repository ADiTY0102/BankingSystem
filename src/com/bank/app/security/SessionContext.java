package com.bank.app.security;

import com.bank.app.enums.Role;

public class SessionContext {

    private static Role currentRole;

    public static void loginAs(Role role) {
        currentRole = role;
    }

    public static Role getRole() {
        return currentRole;
    }

    public static boolean isAdmin() {
        return currentRole == Role.ADMIN;
    }

    public static void requireAdmin() {
        if (!isAdmin()) {
            throw new SecurityException("Access denied: Admin only operation");
        }
    }
}
