package com.hazelcast.simulator.vendors;

import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.simulator.agent.workerprocess.WorkerParameters;
import com.hazelcast.simulator.common.SimulatorProperties;
import com.hazelcast.simulator.coordinator.registry.AgentData;
import com.hazelcast.simulator.utils.SimulatorUtils;
import org.apache.ignite.Ignite;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.File;
import java.util.Collections;
import java.util.Map;

import static com.hazelcast.simulator.TestEnvironmentUtils.createAgentsFileWithLocalhost;
import static com.hazelcast.simulator.TestEnvironmentUtils.localResourceDirectory;
import static com.hazelcast.simulator.TestEnvironmentUtils.setupFakeEnvironment;
import static com.hazelcast.simulator.TestEnvironmentUtils.tearDownFakeEnvironment;
import static com.hazelcast.simulator.utils.FileUtils.deleteQuiet;
import static com.hazelcast.simulator.utils.FileUtils.ensureExistingFile;
import static com.hazelcast.simulator.utils.FileUtils.fileAsText;
import static com.hazelcast.simulator.utils.FileUtils.getUserDir;
import static com.hazelcast.simulator.utils.FileUtils.writeText;
import static com.hazelcast.simulator.utils.FormatUtils.NEW_LINE;
import static java.util.Arrays.asList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertNotNull;

public class HazelcastDriverTest {

    private AgentData agent;
    private SimulatorProperties simulatorProperties;

    @BeforeClass
    public static void beforeClass() throws Exception {
        setupFakeEnvironment();
        createAgentsFileWithLocalhost();
    }

    @AfterClass
    public static void afterClass() {
        tearDownFakeEnvironment();
    }

    @Before
    public void before(){
        simulatorProperties = new SimulatorProperties();
        agent = new AgentData(1, SimulatorUtils.localIp(), SimulatorUtils.localIp());
    }

    @Test
    public void test() throws Exception {
        VendorDriver<HazelcastInstance> driverAtCoordinator = new HazelcastDriver()
                .setAll(simulatorProperties.asPublicMap())
                .setAgents(singletonList(agent))
                .set("CONFIG", fileAsText(localResourceDirectory() + "/hazelcast.xml"));

        WorkerParameters workerParameters = driverAtCoordinator.loadWorkerParameters("member");
        for(Map.Entry<String,String> entry: workerParameters.entrySet()){
            String key = entry.getKey();
            if (key.startsWith("file:")) {
                writeText(entry.getValue(), new File(getUserDir(), key.substring(5, key.length())));
            }
        }

        VendorDriver<HazelcastInstance> driverAtWorker = new HazelcastDriver()
                .setAll(workerParameters.asMap());

        driverAtWorker.createVendorInstance();
        HazelcastInstance hz = driverAtWorker.getInstance();
        assertNotNull(hz);
        driverAtWorker.close();
    }
}