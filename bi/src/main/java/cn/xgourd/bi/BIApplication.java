/**
 * create this file at 09:36:45 by renhd.
 */
package cn.xgourd.bi;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;
import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import io.modelcontextprotocol.client.McpSyncClient;
import lombok.AllArgsConstructor;

/**
 * @author 任海东
 * @since 2025年6月24日
 */
@SpringBootApplication
@RestController
@AllArgsConstructor
public class BIApplication {

	private final List<McpSyncClient> mcpClients;

	private final ChatClient.Builder builder;

	public static void main(final String[] args) {
		SpringApplication.run(BIApplication.class, args);
	}

	@GetMapping("/bi")
	public ResponseEntity<InputStreamResource> generate(
			@RequestParam(value = "msg", defaultValue = "近7天交易金额趋势") final String msg) {
		final String prompt = """
				请根据用户输入信息: %s (e.g. 近7天交易金额趋势)
				1. 选择合适的工具查询数据;
				2. 将查询的数据作为参数，根据<type>选择合适工具生成图表，并返回图片url;
				3. 输出格式：key为url的json字符串。
				""".formatted(msg);
		final var chatClient = builder.defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpClients)).build();
		final String res = chatClient.prompt(prompt).call().content();
		final String url = JSON.parseObject(res).getString("url");
		Assert.hasLength(url, "未解析出正确的图片");
		return url2Resp(url);
	}

	/**
	 * 将图片url转换为响应实体
	 *
	 * @param url
	 * @return
	 */
	private ResponseEntity<InputStreamResource> url2Resp(final String url) {
		try {
			final URLConnection conn = new URI(url).toURL().openConnection();
			return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG)
					.body(new InputStreamResource(conn.getInputStream()));
		} catch (IOException | URISyntaxException e) {
			throw new RuntimeException("系统处理失败: " + e.getMessage(), e);
		}

	}

}
