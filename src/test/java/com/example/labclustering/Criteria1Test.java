package com.example.labclustering;

import org.junit.Test;

import static com.example.labclustering.WrapperClusterNode.HELLO_MESSAGE;
import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertEquals;

public class Criteria1Test extends AbstractCriteriaTest {

  /**
   * Criteria 1: Some nodes may start at the exact same time as others.
   */
  @Test
  public void shouldPrintOnlyOneMessageAfterClusterStart() throws InterruptedException {
    //ensure output stream has cleaned up
    outContent.reset();

    Runnable runnable = WrapperClusterNode.createRunnableForInstance(this.config);

    //Start instances without delay
    Thread[] threads = new Thread[TEST_CLUSTER_SIZE];
    for (int i = 0; i < TEST_CLUSTER_SIZE; i++) {
      Thread thread = new Thread(runnable);
      threads[i] = thread;
      thread.start();
    }

    //wait some to allow thread to start
    MILLISECONDS.sleep(100);

    //wait for threads to finish
    for (int i = 0; i < TEST_CLUSTER_SIZE; i++) {
      threads[i].join();
    }

    //check the system out
    assertEquals("There should be only one '" + HELLO_MESSAGE + "' message in the console.",
            HELLO_MESSAGE, outContent.toString().trim());
  }

}
