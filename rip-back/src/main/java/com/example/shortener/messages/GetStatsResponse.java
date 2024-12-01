package com.example.shortener.messages;

import lombok.Data;

import java.util.Date;

public record GetStatsResponse(Date creationDate, long usageCount) {
}
