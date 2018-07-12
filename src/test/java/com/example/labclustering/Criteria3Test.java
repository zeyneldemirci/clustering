package com.example.labclustering;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.junit.Test;

import static com.example.labclustering.WrapperClusterNode.HELLO_MESSAGE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class Criteria3Test extends AbstractCriteriaTest {

  /**
   * Criteria 3: Some nodes may restart before others start a first time.
   * <p>
   * This test may not meet criteria very well I think because of testing limitations.
   */
  @Test
  public void shouldPrintOnlyOneMessageIfOneOrMoreNodeRestarted() throws InterruptedException {
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

    //wait some before terminate the first node
    SECONDS.sleep(5);

    //Terminate first node
    HazelcastInstance firstNode = Hazelcast.getAllHazelcastInstances().iterator().next();
    firstNode.getLifecycleService().terminate();
    threads[0].interrupt();

    //start new node
    threads[0] = new Thread(runnable);
    threads[0].start();

    //wait for threads to finish
    for (int i = 0; i < TEST_CLUSTER_SIZE; i++) {
      threads[i].join();
    }

    //check the system out
    assertEquals("There should be only one '" + HELLO_MESSAGE + "' message in the console.",
            HELLO_MESSAGE, outContent.toString().trim());
  }

}
