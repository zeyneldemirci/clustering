package com.example.labclustering;

import com.hazelcast.cluster.ClusterState;
import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;

import java.util.Iterator;
import java.util.concurrent.locks.Lock;

import static java.util.concurrent.TimeUnit.SECONDS;

/**
 * This class just starts {@link HazelcastInstance} in a new thread
 * <p>
 * If the cluster at configured size then prints "We are started!" message to the console only in the first node.
 */
class WrapperClusterNode extends Thread {
  static final String HELLO_MESSAGE = "We are started!";

  WrapperClusterNode(Config config) {
    super(createRunnableForInstance(config));
    this.start();
  }

  static boolean isFirstMemberInTheCluster(HazelcastInstance hazelcastInstance) {
    Iterator<Member> iterator = hazelcastInstance.getCluster().getMembers().iterator();
    return (iterator.hasNext() && iterator.next().localMember());

  }

  static Runnable createRunnableForInstance(Config config) {
    return () -> {
      HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);

      //wait some to allow other instances to start join the cluster
      try {
        SECONDS.sleep(20);
      } catch (InterruptedException e) {
        //ignored
      }

      do {
        if (hazelcastInstance.getCluster().getClusterState().equals(ClusterState.ACTIVE) &&
                hazelcastInstance.getPartitionService().isClusterSafe()) {

          if (isFirstMemberInTheCluster(hazelcastInstance)) {
            System.out.println(HELLO_MESSAGE);
          }
          break;
        }
      } while (true);
    };
  }

}
