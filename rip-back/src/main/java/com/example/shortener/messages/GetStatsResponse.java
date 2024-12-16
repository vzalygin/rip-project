package com.example.shortener.messages;

public record GetStatsResponse(long id, String creationDate, long usageCount) {
}
