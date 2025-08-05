/**
 * create this file at 10:33:16 by renhd.
 */
package cn.xgourd.multimodality;

import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.content.Media;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.MimeTypeUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.alibaba.cloud.ai.dashscope.audio.DashScopeSpeechSynthesisModel;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisPrompt;
import com.alibaba.cloud.ai.dashscope.audio.synthesis.SpeechSynthesisResponse;
import com.alibaba.cloud.ai.dashscope.chat.DashScopeChatModel;
import com.alibaba.fastjson.JSON;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 任海东
 * @since 2025年6月27日
 */
@RestController
@SpringBootApplication
@AllArgsConstructor
@Slf4j
public class MultiModalityApplication {

	private final DashScopeChatModel chatModel;

	private final DashScopeSpeechSynthesisModel speechModel;

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		SpringApplication.run(MultiModalityApplication.class, args);
	}

	@PostMapping("/img2voice")
	public ResponseEntity<byte[]> transfer(final MultipartFile file,
			@RequestParam(value = "msg", defaultValue = "") final String msg) {
		final String prompt = """
				请根据用户要求：%s 识别图片内容并输出key为content的json字符串。
				""".formatted(msg);
		final UserMessage message = UserMessage.builder().text(prompt)
				.media(new Media(MimeTypeUtils.IMAGE_PNG, file.getResource())).build();
		final String chatResp = chatModel.call(message).replace("```json", "").replace("```", "").trim();
		log.info("识别结果: {}", chatResp);
		final SpeechSynthesisResponse speechResp = speechModel
				.call(new SpeechSynthesisPrompt(JSON.parseObject(chatResp).getString("content")));
		return ResponseEntity.ok().contentType(MediaType.parseMediaType("audio/mpeg"))
				.body(speechResp.getResult().getOutput().getAudio().array());
	}

}
