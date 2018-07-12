package com.example.labclustering;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.Member;
import com.hazelcast.spi.properties.GroupProperty;

import java.util.Iterator;
import java.util.Properties;

/**
 * This class just starts {@link HazelcastInstance} in a new thread
 *
 * If the cluster quorum reaches at configured size then prints "We are started!" message to the console.
 */
public class WrapperClusterNode extends Thread {
  static final String HELLO_MESSAGE = "We are started!";
  private final Config config;

  public WrapperClusterNode(Config config)  {
    this.config = config;
    new Thread(this).start();
  }

  @Override
  public void run() {
    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
    //configured value
    Properties props = hazelcastInstance.getConfig().getProperties();
    int minClusterSizeConfig = Integer.parseInt(props.getProperty(GroupProperty.INITIAL_MIN_CLUSTER_SIZE.getName()));
    //actual value
    int currentClusterSizeConfig = hazelcastInstance.getCluster().getMembers().size();

    //If it is the first member of the cluster and if the cluster size reaches at the configured value then print message
    if (isFirstMemberInTheCluster(hazelcastInstance) && currentClusterSizeConfig == minClusterSizeConfig) {
      System.out.println(HELLO_MESSAGE);
    }
  }

  private boolean isFirstMemberInTheCluster(HazelcastInstance hazelcastInstance) {
    Iterator<Member> iterator = hazelcastInstance.getCluster().getMembers().iterator();
    return (iterator.hasNext() && iterator.next().localMember());

  }
}
