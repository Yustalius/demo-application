package sdb.utils;

import org.awaitility.core.ThrowingRunnable;

import java.util.concurrent.TimeUnit;

import static org.awaitility.Awaitility.await;

public class AwaitUtils {

  public static void waitFor(ThrowingRunnable assertion) {
    waitFor(assertion, 1, 5);
  }

  public static void waitFor(ThrowingRunnable assertion, int pollIntervalSeconds, int maxWaitSeconds) {
    await()
        .pollInterval(pollIntervalSeconds, TimeUnit.SECONDS)
        .atMost(maxWaitSeconds, TimeUnit.SECONDS)
        .ignoreExceptions()
        .untilAsserted(assertion);
  }
}
