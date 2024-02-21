package com.relogiclabs.jschema.internal.util;

import org.apache.commons.lang3.StringUtils;

import java.util.Collection;
import java.util.stream.Stream;

import static java.util.stream.Collectors.joining;

public final class StringHelper {
    private StringHelper() {
        throw new UnsupportedOperationException();
    }

    public static String toEncoded(String value)
    {
        var builder = new StringBuilder();
        value = unquote(value);

        for(int i = 0; i < value.length(); i++) {
            char current = value.charAt(i);
            if(current == '\\') {
                char next = value.charAt(i + 1);
                switch(next) {
                    case '"': builder.append('"'); i++; break;
                    case '\\': builder.append('\\'); i++; break;
                    case '/': builder.append('/'); i++; break;
                    case 'b': builder.append('\b'); i++; break;
                    case 'f': builder.append('\f'); i++; break;
                    case 'n': builder.append('\n'); i++; break;
                    case 'r': builder.append('\r'); i++; break;
                    case 't': builder.append('\t'); i++; break;
                    case 'u': builder.append((char) (int) Integer
                            .valueOf(value.substring(i + 2, i + 6), 16));
                            i += 5; break;
                }
            } else builder.append(current);
        }
        return builder.toString();
    }

    public static String quote(Object source) {
        return '"' + source.toString() + '"';
    }

    public static String unquote(String target) {
        if(target.startsWith("\"") && target.endsWith("\""))
            return StringUtils.substring(target, 1, -1);
        return target;
    }

    public static String join(Collection<?> list, String delimiter,
                              String prefix, String suffix) {
        var result = list.stream().map(Object::toString).collect(joining(delimiter));
        if(!result.isEmpty()) return prefix + result + suffix;
        return result;
    }

    public static String joinWith(Collection<?> list, String delimiter,
                                  String prefix, String suffix) {
        var result = list.stream().map(Object::toString).collect(joining(delimiter));
        return prefix + result + suffix;
    }

    public static String joinWith(Stream<?> stream, String delimiter,
                                  String prefix, String suffix) {
        var result = stream.map(Object::toString).collect(joining(delimiter));
        return prefix + result + suffix;
    }

    public static String join(Collection<?> list, String delimiter, String prefix) {
        return join(list, delimiter, prefix, "");
    }

    public static String join(Collection<?> list, String delimiter) {
        return join(list, delimiter, "", "");
    }

    public static String concat(Object s1, Object s2) {
        return s1.toString().concat(s2.toString());
    }

    public static String concat(Object s1, Object s2, Object s3) {
        var builder = new StringBuilder();
        return builder.append(s1).append(s2).append(s3).toString();
    }

    public static String concat(Object s1, Object s2, Object s3, Object s4) {
        var builder = new StringBuilder();
        return builder.append(s1).append(s2).append(s3).append(s4).toString();
    }

    public static String concat(Object s1, Object s2, Object s3, Object s4, Object s5) {
        var builder = new StringBuilder();
        return builder.append(s1).append(s2).append(s3).append(s4).append(s5).toString();
    }

    public static String concat(Object s1, Object s2, Object s3, Object s4, Object s5, Object s6) {
        var builder = new StringBuilder();
        return builder.append(s1).append(s2).append(s3).append(s4).append(s5).append(s6).toString();
    }

    public static String concat(Object s1, Object s2, Object s3, Object s4,
                                Object s5, Object s6, Object s7) {
        var builder = new StringBuilder();
        return builder.append(s1).append(s2).append(s3).append(s4)
                .append(s5).append(s6).append(s7).toString();
    }

    public static String concat(Object s1, Object s2, Object s3, Object s4,
                                Object s5, Object s6, Object s7, Object s8) {
        var builder = new StringBuilder();
        return builder.append(s1).append(s2).append(s3).append(s4)
                .append(s5).append(s6).append(s7).append(s8).toString();
    }

    public static String concat(Object... objects) {
        var builder = new StringBuilder();
        for(Object o : objects) builder.append(o);
        return builder.toString();
    }
}