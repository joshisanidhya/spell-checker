package com.shuddhai.nlp;

public final class EditDistance {
    private EditDistance() { }

    public static int calculate(String first, String second) {
        int[] a = first.codePoints().toArray();
        int[] b = second.codePoints().toArray();
        int[] previous = new int[b.length + 1];
        int[] current = new int[b.length + 1];
        for (int j = 0; j <= b.length; j++) previous[j] = j;
        for (int i = 1; i <= a.length; i++) {
            current[0] = i;
            for (int j = 1; j <= b.length; j++) {
                int cost = a[i - 1] == b[j - 1] ? 0 : 1;
                current[j] = Math.min(Math.min(current[j - 1] + 1, previous[j] + 1), previous[j - 1] + cost);
            }
            int[] swap = previous; previous = current; current = swap;
        }
        return previous[b.length];
    }

    public static double similarity(String word, String candidate) {
        int length = Math.max(word.codePointCount(0, word.length()), candidate.codePointCount(0, candidate.length()));
        return length == 0 ? 1.0 : 1.0 - (double) calculate(word, candidate) / length;
    }
}