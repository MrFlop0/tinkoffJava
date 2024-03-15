package edu.java.utils;

import lombok.experimental.UtilityClass;
import org.apache.commons.lang3.tuple.Pair;

@UtilityClass
@SuppressWarnings("MagicNumber")
public class GithubLinkParser {

    public static Pair<String, String> parse(String link) {
        String[] parts = link.split("/");
        if (parts.length < 5 || !parts[0].equals("https:") || !parts[2].equals("github.com")) {
            return null;
        }
        return Pair.of(parts[3], parts[4]);
    }
}
