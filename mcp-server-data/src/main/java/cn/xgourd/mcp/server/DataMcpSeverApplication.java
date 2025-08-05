/**
 * create this file at 11:19:03 by renhd.
 */
package cn.xgourd.mcp.server;

import org.springframework.ai.tool.ToolCallbackProvider;
import org.springframework.ai.tool.method.MethodToolCallbackProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

/**
 * @author 任海东
 * @since 2025年6月24日
 */
@SpringBootApplication
public class DataMcpSeverApplication {

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		SpringApplication.run(DataMcpSeverApplication.class, args);
	}

	@Bean
	public ToolCallbackProvider dataTool(final DataService dataService) {
		return MethodToolCallbackProvider.builder().toolObjects(dataService).build();
	}

}
