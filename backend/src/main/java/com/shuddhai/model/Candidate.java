package com.shuddhai.model;

public record Candidate(String word, int editDistance, double editSimilarity, int frequency, double frequencyScore, double finalScore) { }