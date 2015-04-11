package com.easytoolsoft.concurrentprogramming.ch1;

import java.util.List;

import com.easytoolsoft.concurrentprogramming.util.SinaFinanceUtils;

/**
 * 
 * 顺序执行计算用户股票的总资产净值
 *
 */
public class SequentialNetAssetValue extends AbstractNetAssetValue {
	@Override
	public double computeNetAssetValue(final List<String> tickers) throws Exception {
		double netAssetValue = 0.0;
		for (String ticker : tickers) {
			netAssetValue += 1000 * SinaFinanceUtils.getCurrentStockPrice(ticker);
		}
		return netAssetValue;
	}

	public static void main(final String[] args) {
		new SequentialNetAssetValue().run();
	}
}
