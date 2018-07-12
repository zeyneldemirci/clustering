package com.example.labclustering;

import org.junit.Test;

import java.util.Random;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class Criteria2Test extends AbstractCriteriaTest {

  /**
   * Criteria 2: Some nodes may start seconds or minutes later than others.
   */
  @Test
  public void shouldPrintOnlyOneMessageIfOneOrMoreNodeDelayedToStart() throws InterruptedException {
    //ensure output stream has cleaned up
    outContent.reset();

    Runnable runnable = WrapperClusterNode.createRunnableForInstance(this.config);

    //Start instances with delay
    Thread[] threads = new Thread[TEST_CLUSTER_SIZE];
    for (int i = 0; i < TEST_CLUSTER_SIZE; i++) {
      Thread thread = new Thread(runnable);
      threads[i] = thread;
      //immediate start for the first node
      if (i != 0) {
        SECONDS.sleep(new Random().nextInt(10));
      }
      thread.start();
    }

    //wait for threads to finish
    for (int i = 0; i < TEST_CLUSTER_SIZE; i++) {
      threads[i].join();
    }

    //check system out
    assertEquals("There should be only one '" + WrapperClusterNode.HELLO_MESSAGE + "' message in the console.",
            WrapperClusterNode.HELLO_MESSAGE, outContent.toString().trim());
  }
}
