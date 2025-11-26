package com.dasmo.drs_api.controller;

import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import com.dasmo.drs_api.dto.GreetDto;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class TestWsController {

	private final SimpMessagingTemplate messagingTemplate;

	@MessageMapping("/greet")
	@SendTo("/topic/notification")
	public GreetDto handleGreetings(GreetDto dto) {
		System.out.println("/////////////////////////////////////////////////////////////////" + dto.getName());
		return dto;
	}
}
