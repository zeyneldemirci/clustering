package com.example.labclustering;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.spi.properties.GroupProperty;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.util.Set;

import static java.util.concurrent.TimeUnit.MILLISECONDS;
import static org.junit.Assert.assertEquals;

public class Criteria3Test extends AbstractCriteriaTest {

  /**
   * Criteria 3: Some nodes may restart before others start a first time.
   *
   * This test may not meet criteria very well I think because of testing limitations.
   */
  @Test
  public void shouldPrintOnlyOneMessageIfOneOrMoreNodeRestarted() {
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
    Set<HazelcastInstance> allHazelcastInstances = Hazelcast.getAllHazelcastInstances();

    //first assert should be ok
    assertEquals("There should be only one '" + WrapperClusterNode.HELLO_MESSAGE + "' message in the console.",
            WrapperClusterNode.HELLO_MESSAGE, outContent.toString().trim());

    //After cluster started shutdown first member
    allHazelcastInstances.iterator().next().getLifecycleService().terminate();

    //start new instance
    new WrapperClusterNode(config);

    //sleep some to allow new node to start
    try {
      Thread.sleep(100);
    } catch (InterruptedException e) {
      e.printStackTrace();
    }

    //will wait here until cluster has formed
    Hazelcast.getAllHazelcastInstances();

    //We expect the hello message again because shutdown one one and then restart one
    assertEquals("There should be again '" + WrapperClusterNode.HELLO_MESSAGE + "' message in the console.",
            WrapperClusterNode.HELLO_MESSAGE, outContent.toString().trim());
  }

}
