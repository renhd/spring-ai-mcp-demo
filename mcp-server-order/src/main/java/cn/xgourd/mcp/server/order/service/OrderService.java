/**
 * create this file at 14:00:01 by renhd.
 */
package cn.xgourd.mcp.server.order.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Map;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.ai.tool.annotation.ToolParam;
import org.springframework.stereotype.Service;

import cn.xgourd.mcp.server.order.entity.Order;
import cn.xgourd.mcp.server.order.repository.OrderRepository;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * @author 任海东
 * @since 2025年7月10日
 */
@Service
@Slf4j
@AllArgsConstructor
public class OrderService {

	private final OrderRepository orderRepository;

	@AllArgsConstructor
	@Getter
	enum State {
		/**
		 * 等待支付中
		 */
		WAITING("0"),
		/**
		 * 已取消
		 */
		CANCEL("1"),
		/**
		 * 已支付
		 */
		PAYED("2"),
		/**
		 * 已退款
		 */
		REFUND("3"),
		/**
		 * 已超时
		 */
		TIMEOUT("4");

		private final String code;
	}

	/**
	 * 模拟某软件商品三种套餐及价格
	 */
	private static final Map<String, BigDecimal> PKGS = Map.of("1", BigDecimal.valueOf(9.9), "2",
			BigDecimal.valueOf(29.9), "3", BigDecimal.valueOf(99.9));

	@Tool(description = "根据客户购买的套餐，本地生成一笔待支付订单")
	public Order createOrder(@ToolParam(description = "客户手机号") final String phoneNo,
			@ToolParam(description = "购买套餐的数字编号") final String pkgNo) {
		return orderRepository.save(new Order().setOrderNo(System.currentTimeMillis() + "").setProductNo(pkgNo)
				.setOrderAmount(PKGS.get(pkgNo)).setCustomerId(phoneNo).setCreateTime(new Date())
				.setOrderStatus(State.WAITING.getCode()));
	}

	@Tool(description = "完成本地订单，更新本地订单状态为已支付, 并记录支付宝交易号")
	public void complete(@ToolParam(description = "本地订单号，即商户订单号") final String orderNo,
			@ToolParam(description = "支付宝订单号") final String alipayTradeNo) {
		final Order order = orderRepository.findByOrderNo(orderNo).orElseThrow();
		orderRepository.save(
				order.setOrderStatus(State.PAYED.getCode()).setAlipayTradeNo(alipayTradeNo).setUpdateTime(new Date()));
	}

	@Tool(description = "取消本地订单，更新本地订单状态为已取消")
	public void cancel(final String orderNo) {
		final Order order = orderRepository.findByOrderNo(orderNo).orElseThrow();
		orderRepository.save(order.setOrderStatus(State.CANCEL.getCode()).setUpdateTime(new Date()));
	}

	@Tool(description = "发生退款，更新本地订单状态为已退款")
	public void refund(final String orderNo) {
		final Order order = orderRepository.findByOrderNo(orderNo).orElseThrow();
		orderRepository.save(order.setOrderStatus(State.REFUND.getCode()).setUpdateTime(new Date()));
	}

}
