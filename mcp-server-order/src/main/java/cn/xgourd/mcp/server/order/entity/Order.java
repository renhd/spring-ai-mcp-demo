/**
 * create this file at 14:05:45 by renhd.
 */
package cn.xgourd.mcp.server.order.entity;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author 任海东
 * @since 2025年7月10日
 */
@Entity
@Table(name = "`order`")
@Data
@Accessors(chain = true)
public class Order implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String orderNo; // 订单号|本地订单号|商户订单号

	private String customerId; // 客户ID

	private String productNo; // 产品代码

	private String orderStatus; // 订单状态

	private Date createTime; // 订单日期

	private Date updateTime; // 更新时间

	private BigDecimal orderAmount; // 订单金额

	private String alipayTradeNo; // 支付宝交易号

}
