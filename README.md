# Spring AI MCP 演示项目

一个展示Spring AI与模型上下文协议（MCP）集成构建智能AI应用的综合性演示项目。

## 项目概览

本项目通过多个专门模块展示Spring AI和MCP集成的各个方面，每个模块专注于不同的用例和功能。

## 项目结构

```
spring-ai-mcp-demo/
├── agent-workflow/          # AI代理工作流编排
├── bi/                      # 商业智能集成
├── mcp-server-data/         # 数据操作MCP服务器
├── mcp-server-order/        # 订单管理MCP服务器
├── multi-modality/          # 多模态AI功能
├── order-agent/             # 智能订单处理代理
├── rag/                     # 检索增强生成实现
└── pom.xml                  # 根Maven配置
```

## 模块说明

### 1. agent-workflow - 代理工作流
展示使用Spring AI工作流功能的AI代理工作流编排。
- **主类**: `cn.xgourd.agent.workflow.WorkflowApplication`
- **核心功能**: 路由工作流、代理编排

### 2. bi - 商业智能
商业智能集成模块，用于AI驱动的分析和洞察。
- **主类**: `cn.xgourd.bi.BIApplication`
- **核心功能**: 数据分析、智能报告

### 3. mcp-server-data - 数据MCP服务器
用于数据操作和管理的MCP服务器实现。
- **主类**: `cn.xgourd.mcp.server.DataMcpSeverApplication`
- **核心功能**: 通过MCP进行数据查询、CRUD操作

### 4. mcp-server-order - 订单MCP服务器
用于订单管理和处理的MCP服务器。
- **主类**: `cn.xgourd.mcp.server.order.OrderMcpApplication`
- **核心功能**: 订单CRUD操作、订单生命周期管理
- **实体**: 订单实体与仓库模式

### 5. multi-modality - 多模态功能
展示多模态AI功能（文本、图像、音频处理）。
- **主类**: `cn.xgourd.multimodality.MultiModalityApplication`
- **核心功能**: 多模态内容处理和生成

### 6. order-agent - 订单代理
具有Web界面的智能订单处理代理。
- **主类**: `cn.xgourd.agent.AgentApplication`
- **核心功能**: 订单处理自动化、Web界面、MCP服务器集成
- **配置**: `mcp-servers.json`中的MCP服务器配置
- **Web界面**: `static/index.html`中的静态HTML界面

### 7. rag - 检索增强生成
用于增强AI响应的检索增强生成实现。
- **主类**: `cn.xgourd.rag.RagApplication`
- **核心功能**: 文档检索、上下文感知的AI响应

## 快速开始

### 前置要求
- Java 17或更高版本
- Maven 3.6+
- Spring Boot 3.x
- MCP兼容的AI服务（OpenAI、Anthropic等）

### 快速启动

1. **克隆仓库**
   ```bash
   git clone <仓库地址>
   cd spring-ai-mcp-demo
   ```

2. **构建所有模块**
   ```bash
   mvn clean install
   ```

3. **运行单个模块**
   ```bash
   # 示例：运行订单代理
   cd order-agent
   mvn spring-boot:run
   
   # 示例：运行RAG模块
   cd rag
   mvn spring-boot:run
   ```

### 配置

每个模块都包含自己的`application.yml`用于特定配置。常见配置包括：
- AI服务API密钥和端点
- 数据库连接（如适用）
- MCP服务器配置
- 日志级别

### MCP服务器配置

`order-agent`模块包含`mcp-servers.json`文件用于配置MCP服务器连接：

```json
{
  "mcpServers": {
    "data-server": {
      "command": "java",
      "args": ["-jar", "../mcp-server-data/target/mcp-server-data-*.jar"]
    },
    "order-server": {
      "command": "java",
      "args": ["-jar", "../mcp-server-order/target/mcp-server-order-*.jar"]
    }
  }
}
```

## 开发

### 运行测试
```bash
# 运行所有模块的测试
mvn test

# 运行特定模块的测试
cd <模块名称>
mvn test
```

### 构建
```bash
# 构建所有模块
mvn clean package

# 构建特定模块
cd <模块名称>
mvn clean package
```
