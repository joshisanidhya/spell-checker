package com.shuddhai.nlp;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;

public final class Vocabulary {
    private final Map<String, Integer> frequencies;
    private final int maximumFrequency;

    public Vocabulary(Map<String, Integer> frequencies) {
        this.frequencies = Collections.unmodifiableMap(new LinkedHashMap<>(frequencies));
        this.maximumFrequency = this.frequencies.values().stream().mapToInt(Integer::intValue).max().orElse(0);
    }

    public boolean contains(String word) { return frequencies.containsKey(word); }
    public int getFrequency(String word) { return frequencies.getOrDefault(word, 0); }
    public int getMaxFrequency() { return maximumFrequency; }
    public int size() { return frequencies.size(); }
    public Set<String> words() { return frequencies.keySet(); }
    public Map<String, Integer> getFrequencies() { return frequencies; }
}