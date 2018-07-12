package com.example.labclustering;

import com.hazelcast.config.Config;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.spi.properties.GroupProperty;
import org.junit.After;
import org.junit.Before;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;

public abstract class AbstractCriteriaTest {
  protected static final int INITIAL_MIN_CLUSTER_SIZE = 2;

  protected final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
  protected final PrintStream originalOut = System.out;
  protected Config config;

  @Before
  public void setup() {
    //Wrap console output
    System.setOut(new PrintStream(outContent));

    //create cluster configuration
    config = new Config();
    //set cluster size
    config.setProperty(GroupProperty.INITIAL_MIN_CLUSTER_SIZE.getName(), String.valueOf(INITIAL_MIN_CLUSTER_SIZE));
    //set logging to none for clear view
    config.setProperty(GroupProperty.LOGGING_TYPE.getName(), "none");

  }

  @After
  public void cleanup() {
    System.setOut(originalOut);
    Hazelcast.shutdownAll();
  }

}
