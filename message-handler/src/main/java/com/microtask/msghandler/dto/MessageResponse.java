package com.microtask.msghandler.dto;

import lombok.Builder;

import java.util.Date;

@Builder
public record MessageResponse(String message, Date createDt) {
}