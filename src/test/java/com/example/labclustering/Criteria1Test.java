package com.example.labclustering;

import com.hazelcast.core.Hazelcast;
import org.junit.Test;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertEquals;

public class Criteria1Test extends AbstractCriteriaTest {

  /**
   * Criteria 1: Some nodes may start at the exact same time as others.
   */
  @Test
  public void shouldPrintOnlyOneMessageAfterClusterStart() {
    //start cluster quorum
    for (int i = 0; i < INITIAL_MIN_CLUSTER_SIZE; i++) {
      new WrapperClusterNode(config);
    }

    //sleep some to allow threads to start at least one node
    try {
      MILLISECONDS.sleep(100);
    } catch (InterruptedException e) {
      //ignored
    }

    //will wait here until cluster has formed
    Hazelcast.getAllHazelcastInstances();

    assertEquals("There should be only one '" + WrapperClusterNode.HELLO_MESSAGE + "' message in the console.",
            WrapperClusterNode.HELLO_MESSAGE, outContent.toString().trim());
  }

}
