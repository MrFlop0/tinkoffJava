package edu.java.utils;

public class StackoverflowLinkParser {

    public static Long parse(String link) {
        String[] parts = link.split("/");
        if (parts.length < 5 || !parts[0].equals("https:") || !parts[2].equals("stackoverflow.com")) {
            return null;
        }
        return Long.parseLong(parts[4]);
    }
}
