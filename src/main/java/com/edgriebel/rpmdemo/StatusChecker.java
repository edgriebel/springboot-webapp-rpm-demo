package com.edgriebel.rpmdemo;

import java.util.ArrayList;
import java.util.Collection;
import java.util.logging.Logger;

import org.springframework.beans.factory.annotation.Value;

public class StatusChecker {
    private static final Logger logger = Logger.getLogger(StatusChecker.class.getName());
    
    @Value("${enabled.probability}")
    private float enabledProbability;
    
    @Value("${hello.values}")
    private String [] helloValues;

    public StatusChecker() {
        logger.fine("StatusChecker constructor");
    }

    public DemoDO getStatus() {
        boolean enabled = Math.random() > enabledProbability;
        
        return new DemoDO()
                .enabled(enabled)
                ;
    }
    
    public DemoDO getHello(String name) {
        if (name == null || name.isEmpty()) {
            int i = (int) (Math.random() * helloValues.length);
            logger.info("Null name passed, returning the " + i + "th value");
            name = helloValues[i];
        }
        return new DemoDO().name("Hello " + name + "!");
    }
    
    public Collection<DemoDO> doStuff(String first, String second, String third) {
        if (first == null || second == null || third == null)
            throw new IllegalArgumentException("first, second, and third must be populated");
        logger.info("Doing stuff to: " + first + ":" + second + ":" + third);
        Collection<DemoDO> rtn = new ArrayList<>();
        rtn.add(new DemoDO().name(first).value(second + ":" + third).enabled(Math.random() > enabledProbability));
        rtn.add(new DemoDO().name(second).value(first + ":" + third).enabled(Math.random() > enabledProbability));
        rtn.add(new DemoDO().name(third).value(first + ":" + second).enabled(Math.random() > enabledProbability));
        
        return rtn;
    }
}
