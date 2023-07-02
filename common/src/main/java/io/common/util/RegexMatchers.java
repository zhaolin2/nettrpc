package io.common.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public abstract class RegexMatchers {

    public static String findFirstMatchGroup(String regex, String input) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(input);

        if (matcher.find()) {
            return matcher.group(1);
        }

        return null;
    }
}
