package com.example.labclustering;

import com.hazelcast.core.Hazelcast;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.SECONDS;
import static org.junit.Assert.assertEquals;

public class Criteria2Test extends AbstractCriteriaTest{

  /**
   * Criteria 2: Some nodes may start seconds or minutes later than others.
   */
  @Test
  public void shouldPrintOnlyOneMessageIfOneOrMoreNodeDelayedToStart() {
    //start cluster quorum
    for (int i = 0; i < INITIAL_MIN_CLUSTER_SIZE; i++) {
      new WrapperClusterNode(config);
      //Put delay between starts
      try {
        SECONDS.sleep(3);
      } catch (InterruptedException e) {

      }
    }

    //will wait here until cluster has formed
    Hazelcast.getAllHazelcastInstances();

    assertEquals("There should be only one '" + WrapperClusterNode.HELLO_MESSAGE + "' message in the console.",
            WrapperClusterNode.HELLO_MESSAGE, outContent.toString().trim());
  }
}
