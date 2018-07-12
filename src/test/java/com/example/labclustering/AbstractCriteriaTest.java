package com.example.labclustering;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.spi.properties.GroupProperty;
import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public abstract class AbstractCriteriaTest {
  static final int TEST_CLUSTER_SIZE = 3;

  final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  private final PrintStream originalOut = System.out;
  Config config;

  @Before
  public void setup() {
    //Wrap console output
    System.setOut(new PrintStream(outContent));

    //create cluster configuration
    config = new Config();
    //set logging to none for clear view
    config.setProperty(GroupProperty.LOGGING_TYPE.getName(), "jdk");

  }

  @After
  public void cleanup() {
    System.setOut(originalOut);
    Hazelcast.shutdownAll();
  }

}
