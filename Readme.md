**Compiling Source**

`cd /path/to/project/dir`

`mvn -DskipTests=true clean package`

**Package Structure**

- Application uses [project.source.dir]/main/resources/cluster-config.xml file from classpath as a default config file.
- After building package there will be application jar and dependencies jars under the "lib" folder in the build target folder.
- Application log files go under the "logs" dir
- If you want change your cluster-config or log4j.properties file you can put them under the 

**Acceptance Criterias**

The application should coordinate between the nodes so that as they are started System.out.println("We are started!") 
is only called exactly once across the whole cluster, whether 1 node or 10 are running. 

**Criteria 1**: Some nodes may start at the exact same time as others.

**Criteria 2**: Some nodes may start seconds or minutes later than others.

**Criteria 3**: Some nodes may restart before others start a first time.
 
**Criteria 4**: Some nodes may never be started at all.

**AdHoc Tests**

Default cluster quorum size is three.

`cd target`

`java -jar lab-clustering-0.0.1-SNAPSHOT.jar` (_Run this command in three different console_)

You can see the expected "We are started!" message in only one console tab for the first 3 criteria.


**Running Maven Test Cases** 

`Please run maven tests independently because cleanup procedure does not work correctly.
Probably, start-shutdown-start-shutdown-start cycles does not clean JVM between test cases.
Most of time, they all run well but sometimes fail.
`

_WARNING: You will not see expected message in the console because System.out redirected to the in-memory output stream for testing purpose._

**`mvn clean`**

**`mvn -Dtest=Criteria1Test test`**

**`mvn -Dtest=Criteria2Test test`**

**`mvn -Dtest=Criteria3Test test`**

**`mvn -Dtest=Criteria4Test test`**

`  
   @After

   public void cleanup() {
   
     System.setOut(originalOut);
     
     //Shutdown process does not clean instances from JVM
     Set<HazelcastInstance> allHazelcastInstances = Hazelcast.getAllHazelcastInstances();
     allHazelcastInstances.iterator().forEachRemaining(instance -> instance.getLifecycleService().terminate());
     
     //Below command is same
     Hazelcast.shutdownAll();
   }
`

 