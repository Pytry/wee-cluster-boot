package hoopes.keith.examples.hazelcast.simplecluster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * As I began building this out, I started to design the application
 * with extensibility and modularity in mind. To that end I focused
 * a lot on how I would want to configure a Spring Boot Starter
 * for Hazelcast. This is not fully realized, given the time constraints,
 * and I think there would be a lot more organization needed to get it
 * working well, and easy to understand. To help with at least the idea,
 * I followed a simple rule.
 *
 * Configuration should be concise, with as few class level annotations
 * as possible.
 *
 * Copyright ${year}
 *
 * @author J. Keith Hoopes
 */
@SpringBootApplication
public class WeeClusterBootApplication{

    public static void main(final String[] args){

        SpringApplication.run(WeeClusterBootApplication.class, args);
    }
}
