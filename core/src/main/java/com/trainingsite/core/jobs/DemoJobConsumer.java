package com.trainingsite.core.jobs;

import org.apache.sling.event.jobs.Job;
import org.apache.sling.event.jobs.consumer.JobConsumer;
import org.osgi.service.component.annotations.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Component(service=JobConsumer.class, property= {
        JobConsumer.PROPERTY_TOPICS + "=demo/job/consumer"
})
public class DemoJobConsumer implements  JobConsumer{

    private static final Logger log = LoggerFactory.getLogger(DemoJobConsumer.class);

    @Override
    public JobResult process(Job job) {
        log.info("Demo job is executing");
        return JobConsumer.JobResult.OK;
    }
}
