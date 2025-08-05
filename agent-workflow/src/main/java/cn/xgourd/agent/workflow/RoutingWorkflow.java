/**
 * create this file at 14:34:13 by renhd.
 */
package cn.xgourd.agent.workflow;

import java.util.Map;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.util.Assert;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 任海东
 * @since 2025年7月1日
 */
@Slf4j
@AllArgsConstructor
public class RoutingWorkflow {

	private final ChatClient chatClient;

	private final Map<String, String> routes;

	private static final Map<String, String> DEFAULT_ROUTES = Map.of("poet", "你是一名诗人，请根据用户输入作答", "coder",
			"你是一名程序员，你擅长编码，请根据用户输入写一段程序", "math", "你是一名数学家，请根据用户输入解答", "general", "你是一名客服，请根据用户输入作答");

	public RoutingWorkflow(final ChatClient chatClient) {
		this(chatClient, DEFAULT_ROUTES);
	}

	public record RoutingResponse(String reason, String routeKey) {
	}

	public String route(final String input) {
		final RoutingResponse route = determineRoute(input);
		Assert.notNull(route, "没有找到合适的路由");
		log.info("选择的路由键: {}, 原因: {}", route.routeKey, route.reason);
		final String selectedPrompt = routes.get(route.routeKey);
		return chatClient.prompt(selectedPrompt + "\n输入: " + input).call().content();
	}

	/**
	 * @param input
	 * @param keySet
	 * @return
	 */
	private RoutingResponse determineRoute(final String input) {
		final String selectorPrompt = """
				请根据用户输入的内容，必须在 %s 中选择最合适的路由键并解释下选择的原因，使用json格式返回：
				{"reason":"你选择这个路由的原因","routeKey":"选择的路由键"}
				输入: %s""".formatted(routes.keySet(), input);
		return chatClient.prompt(selectorPrompt).call().entity(RoutingResponse.class);
	}

}
