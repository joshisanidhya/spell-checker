package com.shuddhai.model;

import java.util.List;

public record SpellCheckResponse(String text, List<Correction> errors, long processingTimeMs) { }