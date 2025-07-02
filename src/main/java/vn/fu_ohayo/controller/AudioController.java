package vn.fu_ohayo.controller;

import com.google.api.client.util.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;

@RestController
@RequestMapping("/api/audio")
public class AudioController {

    @Value("${azure.speech.key}")
    private String azureKey;

    @Value("${azure.speech.region}")
    private String azureRegion;

    @PostMapping("/transcribe")
    public ResponseEntity<String> transcribeAudio(@RequestParam MultipartFile audio) throws IOException {
        byte[] audioBytes = audio.getBytes();
        String result = transcribeJapaneseAudio(audioBytes);
        return ResponseEntity.ok(result);
    }

    public String transcribeJapaneseAudio(byte[] audioData) throws UnsupportedEncodingException {
        String endpoint = String.format(
                "https://%s.stt.speech.microsoft.com/speech/recognition/conversation/cognitiveservices/v1?language=ja-JP",
                azureRegion
        );

        // Pronunciation Assessment Config JSON
        String configJson = """
        {
          \"ReferenceText\": \"日本語を勉強しています。\",
          \"GradingSystem\": \"HundredMark\",
          \"Dimension\": \"Comprehensive\",
          \"PhonemeAlphabet\": \"IPA\"
        }
        """;

        String encodedConfig = Base64.getEncoder().encodeToString(configJson.getBytes(StandardCharsets.UTF_8));

        HttpHeaders headers = new HttpHeaders();
        headers.set("Ocp-Apim-Subscription-Key", azureKey);
        headers.set("Pronunciation-Assessment", encodedConfig);
        headers.setContentType(MediaType.valueOf("audio/wav"));

        HttpEntity<byte[]> request = new HttpEntity<>(audioData, headers);
        ResponseEntity<String> response = new RestTemplate().postForEntity(endpoint, request, String.class);

        return response.getBody();
    }
}