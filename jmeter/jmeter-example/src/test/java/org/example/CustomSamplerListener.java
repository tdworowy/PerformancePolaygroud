package org.example;

import org.apache.jmeter.engine.StandardJMeterEngine;
import org.apache.jmeter.engine.event.LoopIterationEvent;
import org.apache.jmeter.engine.event.LoopIterationListener;
import org.apache.jmeter.protocol.http.sampler.HTTPSamplerBase;
import org.apache.jmeter.samplers.SampleEvent;
import org.apache.jmeter.samplers.SampleListener;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jorphan.collections.HashTree;
import org.apache.jorphan.collections.SearchByClass;

import java.net.MalformedURLException;
// TODO fix or remove it, chatGPT can't write it
public class CustomSampleListener implements SampleListener, LoopIterationListener {

    private StandardJMeterEngine engine;

    public CustomSampleListener(StandardJMeterEngine engine) {
        this.engine = engine;
    }

    @Override
    public void sampleOccurred(SampleEvent event) {
        String sampleLabel = event.getResult().getSampleLabel();
        TestElement sampler = findSampler(sampleLabel);

        if (sampler instanceof HTTPSamplerBase) {
            HTTPSamplerBase httpSampler = (HTTPSamplerBase) sampler;
            try {
                System.out.println("HTTP Request: " + httpSampler.getMethod() + " " + httpSampler.getUrl());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }
    }

    private TestElement findSampler(String sampleLabel) {
        HashTree testPlanTree = getTestPlanTree();
        SearchByClass<HTTPSamplerBase> search = new SearchByClass<>(HTTPSamplerBase.class);
        testPlanTree.traverse(search);
        for (HTTPSamplerBase sampler : search.getSearchResults()) {
            if (sampleLabel.equals(sampler.getName())) {
                return sampler;
            }
        }
        return null;
    }

    private HashTree getTestPlanTree() {
        return engine.getTestPlan().getTestTree();
    }


    @Override
    public void sampleStarted(SampleEvent event) {
        // Not used
    }

    @Override
    public void sampleStopped(SampleEvent event) {
        // Not used
    }


    @Override
    public void iterationStart(LoopIterationEvent loopIterationEvent) {

    }
}