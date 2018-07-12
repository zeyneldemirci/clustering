package com.example.labclustering;

import com.hazelcast.core.Hazelcast;
import org.junit.Rule;
import org.junit.Test;
import org.junit.internal.runners.statements.FailOnTimeout;
import org.junit.rules.Timeout;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

public class Criteria4Test extends AbstractCriteriaTest{
  //Assumed that for each member of cluster will start at least in 30 secs
  private static final int TIMEOUT_IN_SECONDS = 30 * INITIAL_MIN_CLUSTER_SIZE;

  /**
   * Criteria 4: Some nodes may never be started at all.
   *
   * It should be timed out after defined timeout rule duration because never reach end
   *
   */
  @Test(expected = TimeoutException.class)
  public void shouldNotPrintMessage() {

    //start cluster quorum minus 1
    for (int i = 0; i < INITIAL_MIN_CLUSTER_SIZE - 1; i++) {
      new WrapperClusterNode(config);
    }

    //sleep some to allow threads to start at least one node
    try {
      MILLISECONDS.sleep(100);
    } catch (InterruptedException e) {
      //ignored
    }

    //will wait here until method has timed out
    Hazelcast.getAllHazelcastInstances();

  }

  @Rule
  public Timeout timeout = new Timeout(TIMEOUT_IN_SECONDS, TimeUnit.SECONDS) {
    public Statement apply(Statement base, Description description) {
      return new FailOnTimeout(base, this.getTimeout(TimeUnit.MILLISECONDS)) {
        @Override
        public void evaluate() throws Throwable {
          try {
            super.evaluate();
            throw new TimeoutException();
          } catch (Exception e) {}
        }
      };
    }
  };

}
