package com.bank.app.util;

public class ConsoleUI {

    public static void printHeader(String title) {
        System.out.println("╔══════════════════════════════════════╗");
        System.out.printf ("║  %-36s║%n", title);
        System.out.println("╚══════════════════════════════════════╝");
    }

    public static void printSeparator() {
        System.out.println("===================================");
    }
}
