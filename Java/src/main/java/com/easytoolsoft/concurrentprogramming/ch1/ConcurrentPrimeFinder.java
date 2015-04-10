package com.easytoolsoft.concurrentprogramming.ch1;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 
 * 并发执行计算出指定范围内(如1-1000万)的素数
 *
 */
public class ConcurrentPrimeFinder extends AbstractPrimeFinder {
	private final int threadPoolSize;
	private final int chunkCount;

	/**
	 * 
	 * @param theThreadPoolSize
	 *            线程池大小
	 * @param theChunkCount
	 *            区间个数
	 */
	public ConcurrentPrimeFinder(final int theThreadPoolSize, final int theChunkCount) {
		this.threadPoolSize = theThreadPoolSize;
		this.chunkCount = theChunkCount;
	}

	@Override
	public int countPrimes(final int number) {
		int count = 0;

		try {
			final List<Callable<Integer>> tasks = new ArrayList<Callable<Integer>>();
			//
			// 每个区间的大小,即有多少个数字
			final int chunkSize = number / chunkCount;
			for (int i = 0; i < chunkCount; i++) {
				//
				// 计算出每个区间的上限与下限数字
				final int lowerNumber = (i * chunkSize) + 1;
				final int upperNumber = (i == chunkCount - 1) ? number : lowerNumber + chunkSize - 1;
				tasks.add(new Callable<Integer>() {
					@Override
					public Integer call() {
						return countPrimesInRange(lowerNumber, upperNumber);
					}
				});
			}

			final ExecutorService executor = Executors.newFixedThreadPool(threadPoolSize);
			final List<Future<Integer>> futures = executor.invokeAll(tasks, 10000, TimeUnit.SECONDS);
			executor.shutdown();

			for (final Future<Integer> future : futures) {
				count += future.get();
			}
		} catch (Exception ex) {
			throw new RuntimeException(ex);
		}

		return count;
	}

	public static void main(final String[] args) {
		// 数字上限，线程池大小，区间个数
		System.out.println("参数格式为: number,threadPoolSize,chunkCount");
		int number = 10000000;// Integer.parseInt(args[0]);
		int threadPoolSize = 4;// Integer.parseInt(args[1]);
		int chunkCount = 4;// Integer.parseInt(args[2]);
		new ConcurrentPrimeFinder(threadPoolSize, chunkCount).run(number);
	}
}