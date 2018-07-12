package com.example.labclustering;

import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import org.junit.Test;

import java.util.Iterator;

import static com.example.labclustering.WrapperClusterNode.HELLO_MESSAGE;
import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class Criteria4Test extends AbstractCriteriaTest {

  /**
   * Criteria 4: Some nodes may never be started at all.
   */
  @Test
  public void shouldPrintOneMessageIfSomeNodeDoNotStart() throws InterruptedException {
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

    //wait some before terminate the a node
    SECONDS.sleep(5);

    //Terminate last node and do not start it again
    Iterator<HazelcastInstance> iterator = Hazelcast.getAllHazelcastInstances().iterator();
    HazelcastInstance instance = null;
    while (iterator.hasNext()) {
      instance = iterator.next();
    }
    if (instance == null) {
      throw new RuntimeException();
    }
    instance.getLifecycleService().terminate();
    threads[TEST_CLUSTER_SIZE - 1].interrupt();

    //wait for threads to finish
    for (int i = 1; i < TEST_CLUSTER_SIZE; i++) {
      threads[i].join();
    }

    //check the system out
    assertEquals("There should be only one '" + HELLO_MESSAGE + "' message in the console.",
            HELLO_MESSAGE, outContent.toString().trim());

  }

}
