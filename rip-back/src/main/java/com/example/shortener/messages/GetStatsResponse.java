package com.example.shortener.messages;

public record GetStatsResponse(long id, String shortKey, String creationDate, long usageCount) {
}
