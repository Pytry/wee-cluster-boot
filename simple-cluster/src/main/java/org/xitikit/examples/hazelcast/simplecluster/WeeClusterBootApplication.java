package org.xitikit.examples.hazelcast.simplecluster;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * As I began building this out, I started to design the application
 * with extensibility and modularity in mind. To that end I focused
 * a lot on how I would want to configure a Spring Boot Starter
 * for Hazelcast. This is not fully realized, given the time constraints,
 * and I think there would be a lot more organization needed to get it
 * working well, and easy to understand.
 *
 * I probably got too carried away with experimenting in different solutions,
 * and trying to show off. In the end I decided to scale back and focus more
 * on the requirements.
 *
 * I still created a "starter" project, but I incorporated more of the configuration
 * that SpringBoot already has implemented.
 *
 * I used two different projects as examples to work from:
 *
 *  https://bitbucket.org/tomask79/spring-leader-hazelcast.git
 *  https://github.com/SpringOnePlatform2016/dsyer-locks-and-leaders
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
