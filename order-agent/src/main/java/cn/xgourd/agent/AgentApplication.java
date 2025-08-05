/**
 * create this file at 17:20:33 by renhd.
 */
package cn.xgourd.agent;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.mcp.SyncMcpToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.alibaba.fastjson.JSON;

import io.modelcontextprotocol.client.McpSyncClient;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 任海东
 * @since 2025年7月10日
 */
@SpringBootApplication
@RestController
@Slf4j
public class AgentApplication {

	private final ChatClient chatClient;

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		SpringApplication.run(AgentApplication.class, args);
	}

	/**
	 * 初始化chatClient
	 *
	 * @param mcpClients
	 * @param builder
	 */
	public AgentApplication(final List<McpSyncClient> mcpClients, final ChatClient.Builder builder) {
		this.chatClient = builder.defaultToolCallbacks(new SyncMcpToolCallbackProvider(mcpClients)).build();
	}

	/**
	 * 客户下单
	 *
	 * @param phone    客户手机号
	 * @param pkgNo    选择的套餐
	 * @param response
	 */
	@PostMapping("/buy")
	public void buy(@RequestParam final String phone, @RequestParam final String plan,
			final HttpServletResponse response) {
		final String prompt = """
				请根据用户输入信息: 手机号: %s, 套餐号: %s
				1. 生成一笔本地订单;
				2. 将订单号、订单金额、订单主题<套餐n:选择的套餐对应的名称>[1-基础套餐、2-专业套餐、3-尊享套餐]作为参数生成支付宝订单，将支付链接<https://xxx>返回;
				3. 输出格式：key为url的可解析的json字符串,eg: {"url":"https://xxx"}
				4. 请注意url的提取末尾不含括号，输出json字符串末尾不含"]";
				""".formatted(phone, plan);
		final String resp = chatClient.prompt(prompt).call().content().replace("```json", "").replace("```", "");
		log.info("生成订单响应: {}", resp);
		final String url = JSON.parseObject(resp).getString("url");
		log.info("支付链接: {}", url);
		try {
			response.sendRedirect(url);
		} catch (final IOException e) {
			log.error("重定向失败", e);
			try {
				response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "重定向失败");
			} catch (final IOException ioException) {
				log.error("发送错误响应失败", ioException);
			}
		}
	}

	/**
	 *
	 * @param params
	 * @return
	 */
	@GetMapping("/callback")
	public String callback(@RequestParam final Map<String, String> params) {
		log.info("订单回调: {}", params);
		// 这里最好还有验签逻辑,回调更新
		final String prompt = """
				根据回调参数: %s,完成本地订单
				1. 将本地订单状态更新为已支付,并记录支付宝交易号;
				2. 请注意本地订单号是out_trade_no, 支付宝交易号是trade_no
				""".formatted(JSON.toJSONString(params));
		return chatClient.prompt(prompt).call().content();
	}

}
