/**
 * create this file at 12:20:16 by renhd.
 */
package cn.xgourd.mcp.server.order;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import cn.xgourd.mcp.server.order.service.OrderService;

/**
 * @author 任海东
 * @since 2025年7月10日
 */
@SpringBootApplication
public class OrderMcpApplication {

	public static void main(final String[] args) {
		SpringApplication.run(OrderMcpApplication.class, args);
	}

	@Bean
	public ToolCallbackProvider dataTool(final OrderService orderService) {
		return MethodToolCallbackProvider.builder().toolObjects(orderService).build();
	}

}
