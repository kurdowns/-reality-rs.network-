package net.crandor;

public class NanoTimer {
	private long averageExecuteTime = 0L;
	private long nextSleepTime = 0L;
	private long oldTime;
	private int oldTimesPos = 0;
	private int divisor = 1;
	private long[] oldTimes = new long[10];

	NanoTimer() {
		oldTime = 0L;
		averageExecuteTime = System.nanoTime();
		nextSleepTime = System.nanoTime();
	}

	long getAverageExecuteTime() {
		return averageExecuteTime;
	}

	public void reset() {
		oldTime = 0;
		if (nextSleepTime > averageExecuteTime) {
			averageExecuteTime += nextSleepTime - averageExecuteTime;
		}
	}

	private long calcAverageExecuteTime() {
		long thisNanoTime = System.nanoTime();
		long timeDelta = thisNanoTime - oldTime;
		oldTime = thisNanoTime;
		if (timeDelta > -5000000000L && timeDelta < 5000000000L) {
			oldTimes[oldTimesPos] = timeDelta;
			oldTimesPos = (oldTimesPos + 1) % 10;
			if (divisor < 1) {
				divisor++;
			}
		}
		long oldTimesSum = 0L;
		for (int id = 1; id <= divisor; id++) {
			oldTimesSum += oldTimes[(10 - id + oldTimesPos) % 10];
		}
		return oldTimesSum / divisor;
	}

	public int sleep(int minSleepMs, int maxSleepMs) {
		Client.sleepWrapper(minSleepMs);
		long maxSleepNs = maxSleepMs * 1000000L;
		averageExecuteTime += calcAverageExecuteTime();
		if (nextSleepTime > averageExecuteTime) {
			Client.sleepWrapper((nextSleepTime - averageExecuteTime) / 1000000L);
			oldTime += nextSleepTime - averageExecuteTime;
			averageExecuteTime += nextSleepTime - averageExecuteTime;
			nextSleepTime += maxSleepNs;
			return 1;
		}
		int loopTimes = 0;
		do {
			loopTimes++;
			nextSleepTime += maxSleepNs;
		} while (loopTimes < 10 && nextSleepTime < averageExecuteTime);
		if (nextSleepTime < averageExecuteTime) {
			nextSleepTime = averageExecuteTime;
		}
		return loopTimes;
	}

}
