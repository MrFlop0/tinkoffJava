package edu.java.domain.dto;

public record LinkInfo(
    String url,
    int type,
    Long starsCount,
    Long answerCount
) {
}
