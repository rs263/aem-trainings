/*
 *  Copyright 2015 Adobe Systems Incorporated
 *
 *  Licensed under the Apache License, Version 2.0 (the "License");
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License.
 */
package com.trainingsite.core.schedulers;

import org.apache.sling.event.jobs.JobManager;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.metatype.annotations.AttributeDefinition;
import org.osgi.service.metatype.annotations.Designate;
import org.osgi.service.metatype.annotations.ObjectClassDefinition;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

/**
 * A simple demo for cron-job like tasks that get executed regularly.
 * It also demonstrates how property values can be set. Users can
 * set the property values in /system/console/configMgr
 */
@Designate(ocd= DemoScheduledTask.Config.class)
@Component(service=Runnable.class)
public class DemoScheduledTask implements Runnable {

    @Reference
    private JobManager jobManager;

    @ObjectClassDefinition(name="A scheduled task",
                           description = "Simple demo for cron-job like task with properties")
    public static @interface Config {

        @AttributeDefinition(name = "Cron-job expression")
        String scheduler_expression() default "0 * * * * ?";

        @AttributeDefinition(name = "Concurrent task",
                             description = "Whether or not to schedule this task concurrently")
        boolean scheduler_concurrent() default false;

        @AttributeDefinition(name = "A parameter",
                             description = "Can be configured in /system/console/configMgr")
        String myParameter() default "";

        @AttributeDefinition(name = "Is Enabled",
                             description = "Check to enable scheduler")
        boolean isEnabled() default false;
    }

    private final Logger logger = LoggerFactory.getLogger(getClass());

    private String myParameter;
    private boolean isEnabled;

    @Override
    public void run() {
        if(isEnabled) {
            logger.info("Demo schedule task is now running, myParameter='{}'", myParameter);
            final Map<String, Object> jobParams = new HashMap<String, Object>();
            jobParams.put("prop1", "something");
            jobManager.addJob("demo/job/consumer", jobParams);
        }
    }

    @Activate
    protected void activate(final Config config) {
        logger.info("Demo task activate method");
        myParameter = config.myParameter();
        isEnabled = config.isEnabled();
    }

}
