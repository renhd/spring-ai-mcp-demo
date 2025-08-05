/**
 * create this file at 11:21:04 by renhd.
 */
package cn.xgourd.mcp.server;

import java.util.List;

import org.springframework.ai.tool.annotation.Tool;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

/**
 * @author 任海东
 * @since 2025年6月24日
 */
@Service
@Slf4j
public class DataService {

	public record IndicatorByDay(String time, double value) {
	}

	public record ChartData(String type, String axisXTitle, String axisYTitle, List<?> data) {
	}

	@Tool(description = "获取近N天某指标数据，指标包括交易金额(trade_amount)、交易笔数(trade_count)、交易手续费(fee)等")
	public ChartData getLastNDaysIndicator(final int days, final String indicator) {
		log.info("查询近{}天的{}数据", days, indicator);
		// 模拟查询数据库select dt as time, sum({indicator}) as value from ads_trade_data where
		// dt >= date_sub(curdate(), interval {days} day) group by dt
		final List<IndicatorByDay> data = List.of(new IndicatorByDay("2025-06-01", 1000.0),
				new IndicatorByDay("2025-06-02", 1200.0), new IndicatorByDay("2025-06-03", 900.0),
				new IndicatorByDay("2025-06-04", 1100.0), new IndicatorByDay("2025-06-05", 1300.0),
				new IndicatorByDay("2025-06-06", 1400.0), new IndicatorByDay("2025-06-07", 1500.0));
		return new ChartData("area", "交易日期", indicator, data);
	}

	@Tool(description = "获取交易排行top10的分公司")
	public String getTop10() {
		// 模拟返回数据
		return "top10";
	}

	// ...更多查询数据的方法

}
