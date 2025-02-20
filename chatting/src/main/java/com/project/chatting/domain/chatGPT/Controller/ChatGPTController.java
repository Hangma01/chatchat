package com.project.chatting.domain.chatGPT.Controller;

import com.project.chatting.domain.chatGPT.dto.RequestChatGPT;
import com.project.chatting.domain.chatGPT.dto.ResponseChatGPT;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/chatgpt")
public class ChatGPTController {
    @Value("${openai.model}")
    private String model;

    @Value("${openai.api.url}")
    private String apiURL;

    private final RestTemplate template;

    @GetMapping("/spellingCheck")
    public ResponseEntity<?> chat(@RequestParam String prompt) {
        RequestChatGPT request = new RequestChatGPT(model, prompt);
        ResponseChatGPT response = template.postForObject(apiURL, request, ResponseChatGPT.class);

        return new ResponseEntity<>(response.getChoices().get(0).getMessage().getContent(), HttpStatus.OK);
    }
}
