package com.example;

import org.apache.jmeter.control.LoopController;
import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.protocol.http.sampler.HTTPSampler;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.testelement.TestPlan;
import org.apache.jmeter.threads.SetupThreadGroup;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.collections.HashTree;
import org.junit.Test;

public class AppTest {

    @Test
    public void exampleTest(){
    //public static void main(String[] args){
        // Engine
        StandardJMeterEngine jm = new StandardJMeterEngine();
        // jmeter.properties
        JMeterUtils.loadJMeterProperties("jmeter.properties");

        HashTree hashTree = new HashTree();     
        //Config 
        
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
        postData.addNonEncodedArgument("body", "{ \"data1\": \"${__RandomString(20,abcdefg)}\", \"data2\":  \"${__RandomString(20,abcdefg)}\"}\"", "");
        

        // Loop Controller
        TestElement loopCtrl = new LoopController();
        ((LoopController)loopCtrl).setLoops(1);
        ((LoopController)loopCtrl).addTestElement(getData);
        ((LoopController)loopCtrl).addTestElement(postData);
        ((LoopController)loopCtrl).setFirst(true);

        // Thread Group
        SetupThreadGroup threadGroup = new SetupThreadGroup();
        threadGroup.setNumThreads(1);
        threadGroup.setRampUp(1);
        threadGroup.setSamplerController((LoopController)loopCtrl);

        // Test plan
        TestPlan testPlan = new TestPlan("TEST PLAN");

        hashTree.add("testPlan", testPlan);
        hashTree.add("loopCtrl", loopCtrl);
        hashTree.add("threadGroup", threadGroup);
        hashTree.add("getData", getData);
        hashTree.add("postData", postData);         

        jm.configure(hashTree);

        jm.run();
    }
}