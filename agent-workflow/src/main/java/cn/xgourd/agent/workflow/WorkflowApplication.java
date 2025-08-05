/**
 * create this file at 14:31:17 by renhd.
 */
package cn.xgourd.agent.workflow;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 任海东
 * @since 2025年7月1日
 */
@SpringBootApplication
@RestController
@AllArgsConstructor
@Slf4j
public class WorkflowApplication {

	private final ChatClient.Builder builder;

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		SpringApplication.run(WorkflowApplication.class, args);
	}

	@GetMapping("/route")
	public String route(@RequestParam(value = "msg") final String msg) {
		log.info("用户输入: {}", msg);
		final String resp = new RoutingWorkflow(builder.build()).route(msg);
		log.info("响应内容: {}", resp);
		return resp;
	}
}
