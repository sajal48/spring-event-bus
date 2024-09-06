package com.sajal.springevents;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.boot.web.context.WebServerInitializedEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.ApplicationListener;
import org.springframework.context.annotation.Bean;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;

@SpringBootApplication
@EnableAsync
public class SpringEventsApplication {

    public static void main(String[] args) {
        SpringApplication.run(SpringEventsApplication.class, args);
    }

    @EventListener
    void serverInitialized(WebServerInitializedEvent event) {
        System.out.println("server initialized on port :" + event.getWebServer().getPort());
    }

    @EventListener
    void ready(ApplicationReadyEvent event) {
        System.out.println("Application ready event 3 " + event.getTimeTaken().toString());
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> applicationReadyEventApplicationListener() {
        return event -> System.out.println("Application ready event 2 " + event.getTimeTaken().toString());
    }

    @Component
    static class MyListener implements ApplicationListener<ApplicationReadyEvent> {

        @Override
        public void onApplicationEvent(ApplicationReadyEvent event) {
            System.out.println("Application ready event " + event.getTimeTaken().toString());
        }
    }

    @Bean
    ApplicationListener<ApplicationReadyEvent> publisher(ApplicationEventPublisher publisher) {
        return args -> {
//            publisher.publishEvent(new MyCustomEvent("event 1 process"));
            System.out.println(Thread.currentThread().getName() + " published called");
        };
    }

    @org.springframework.modulith.events.ApplicationModuleListener
    void onMyCustomEvent(MyCustomEvent myCustomEvent) throws Exception {
        System.out.println("\u001B[33m" +
                Thread.currentThread().getName() + " custom event started : "
                + myCustomEvent.message
                + "\u001B[0m");
        Thread.sleep(20_000);
        System.out.println("\u001B[33m" + "finished" + "\u001B[0m");
    }

    record MyCustomEvent(String message) {
    }


}
