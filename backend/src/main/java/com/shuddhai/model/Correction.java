package com.shuddhai.model;

import java.util.List;

public record Correction(String word, int position, List<Candidate> suggestions) { }