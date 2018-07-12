package com.example.labclustering;

import com.hazelcast.config.ClasspathXmlConfig;
import com.hazelcast.config.Config;
import com.hazelcast.config.XmlConfigBuilder;

import java.io.FileNotFoundException;

public class MultiNodeApp {
  public static void main(String[] args) {
    Config config;
    try {
      config = new XmlConfigBuilder("./config/cluster-config.xml").build();
    } catch (FileNotFoundException e) {
      //ignore set defaults from classpath resource file
      config = new ClasspathXmlConfig("cluster-config.xml");
    }

    new WrapperClusterNode(config);
    new WrapperClusterNode(config);
    new WrapperClusterNode(config);
    new WrapperClusterNode(config);
  }
}
