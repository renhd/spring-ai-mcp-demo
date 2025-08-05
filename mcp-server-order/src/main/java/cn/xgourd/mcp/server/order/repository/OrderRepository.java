/**
 * create this file at 14:18:50 by renhd.
 */
package cn.xgourd.mcp.server.order.repository;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;

import cn.xgourd.mcp.server.order.entity.Order;

/**
 * @author 任海东
 * @since 2025年7月10日
 */
public interface OrderRepository extends CrudRepository<Order, Long> {

	Optional<Order> findByOrderNo(String orderNo);
}
