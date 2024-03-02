package com.example;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.control.gui.TestPlanGui;
import org.apache.jmeter.engine.JMeterEngineException;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.save.SaveService;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.SetupThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

public class AppTest {

    @Test
    public void exampleTest() {
        File properties = new File(
                getClass().getClassLoader().getResource("jmeter.properties").getFile()
        );

        // Engine
        StandardJMeterEngine jm = new StandardJMeterEngine();
        // jmeter.properties
        JMeterUtils.loadJMeterProperties(properties.getPath());
        JMeterUtils.setJMeterHome("D:\\jmeter\\apache-jmeter-5.1.1\\"); //Path to jmeter
        JMeterUtils.initLocale();

        HashTree hashTree = new HashTree();

        // HTTP Sampler
        HTTPSampler getData = new HTTPSampler();
        getData.setDomain("localhost");
        getData.setPort(3000);
        getData.setPath("/");
        getData.setMethod("GET");

        HTTPSampler postData = new HTTPSampler();
        postData.setDomain("localhost");
        postData.setPort(3000);
        postData.setPath("/data");
        postData.setMethod("POST");
        postData.setPostBodyRaw(true);
        postData.addNonEncodedArgument("body", "data1:Test1,data2:Test2", null); // Not exactly as it should be

        // Loop Controller
        LoopController loopCtrl = new LoopController();
        loopCtrl.setLoops(1);
        loopCtrl.addTestElement(getData);
        loopCtrl.addTestElement(postData);
        loopCtrl.setFirst(true);

        // Thread Group
        SetupThreadGroup threadGroup = new SetupThreadGroup();
        threadGroup.setNumThreads(1);
        threadGroup.setRampUp(1);
        threadGroup.setSamplerController(loopCtrl);

        // Test plan
        TestPlan testPlan = new TestPlan("TEST PLAN");
        testPlan.setProperty(TestElement.TEST_CLASS, TestPlan.class.getName());
        testPlan.setProperty(TestElement.GUI_CLASS, TestPlanGui.class.getName());

        hashTree.add(testPlan);
        hashTree.add(testPlan, loopCtrl);
        hashTree.add(testPlan, threadGroup);
        hashTree.add(testPlan, getData);
        hashTree.add(testPlan, postData);

        jm.configure(hashTree);

        try {
            SaveService.saveTree(hashTree, Files.newOutputStream(Paths.get("test.jmx")));
        } catch (IOException e) {
            e.printStackTrace();
        }
        try {
            jm.runTest();
            while (jm.isActive()) {
                Thread.sleep(1000);
            }
        } catch (JMeterEngineException | InterruptedException e) {
            e.printStackTrace();
        }
        //jm.run();

    }
}