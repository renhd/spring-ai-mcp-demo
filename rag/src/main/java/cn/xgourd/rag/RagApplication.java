/**
 * create this file at 10:44:26 by renhd.
 */
package cn.xgourd.rag;

import java.util.List;

import org.springframework.ai.chat.client.ChatClient;
import org.springframework.ai.chat.client.advisor.vectorstore.QuestionAnswerAdvisor;
import org.springframework.ai.document.Document;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import lombok.AllArgsConstructor;
import reactor.core.publisher.Flux;

/**
 * @author 任海东
 * @since 2025年6月25日
 */
@SpringBootApplication
@AllArgsConstructor
@RestController
public class RagApplication {

	private final ChatClient.Builder builder;

	private final VectorStore vectorStore;

	/**
	 * @param args
	 */
	public static void main(final String[] args) {
		SpringApplication.run(RagApplication.class, args);
	}

//	@PostConstruct // 仅执行一次
	public void init() {
		final List<Document> documents = List.of(new Document("表名：user，字段：id, name, age"),
				new Document("表名：role，字段：id, name"), new Document("表名：user_role，字段：id, user_id, role_id"),
				new Document("表名：perm，字段：id, name"), new Document("表名：role_perm，字段：id, role_id, perm_id"),
				new Document("表名：order，字段：id, user_id, create_time(datetime), trade_amount"));
		vectorStore.add(documents);
	}

	@GetMapping("/rag")
	public Flux<String> generate(@RequestParam(value = "msg") final String msg) {
		final String prompt = """
				你是一个专业的SQL工程师，请根据上下文表结构信息：
				<context>
				生成满足问题要求的查询SQL：
				1. 完全符合mysql语法规范
				2. 问题：%s
				3. 特别注意：a.如果未找到上下文请不要自由发挥仅回答: 提供的信息不足，无法生成SQL。 b.输出key为sql的json格式。
				""".formatted(msg);
		final var chatClient = builder.defaultAdvisors(new QuestionAnswerAdvisor(vectorStore)).build();
		return chatClient.prompt(prompt).stream().content();
	}

}
