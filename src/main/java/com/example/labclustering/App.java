package com.example.labclustering;

import com.hazelcast.cluster.ClusterState;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;

import static java.util.concurrent.TimeUnit.SECONDS;

public class App {
  private static final String MAP_NAME = "GREETING";
  private static final String MAP_KEY = "PRINT";


  public static void main(String[] args) throws InterruptedException {
    HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance();

    SECONDS.sleep(10);

    if (hazelcastInstance.getCluster().getClusterState().equals(ClusterState.ACTIVE)) {
      IMap<String, Boolean> map = hazelcastInstance.getMap(MAP_NAME);
      map.lock(MAP_NAME);
      try {
        if (map.get(MAP_KEY) == null ||
                map.get(MAP_KEY).equals(Boolean.FALSE)) {

          System.out.println("We are started!");

          map.put(MAP_KEY, Boolean.TRUE);
        }
      } finally {
        map.unlock(MAP_KEY);
      }
    }
  }
}
