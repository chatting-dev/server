package com.chatting.presentation;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.chatting.application.UserAccountService;
import com.chatting.presentation.dto.request.SignUpRequest;
import com.chatting.presentation.dto.response.SignUpResponse;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@RequestMapping("/api/v1")
@RestController
public class UserAccountController {

	private final UserAccountService userAccountService;

	@PostMapping("/users")
	public ResponseEntity<SignUpResponse> signUp(@RequestBody final SignUpRequest request) {
		SignUpResponse response = userAccountService.signUp(request);
		return ResponseEntity
			.ok()
			.body(response);
	}
}
