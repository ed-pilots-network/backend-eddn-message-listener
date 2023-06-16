module edpn.eddnMessageListener {
    requires static lombok;

    requires spring.retry;
    requires spring.messaging;
    requires spring.core;
    requires org.slf4j;
    requires com.fasterxml.jackson.annotation;
    requires com.fasterxml.jackson.databind;
    requires spring.beans;
    requires spring.context;
    requires spring.kafka;
    requires kafka.clients;
    requires spring.boot;
    requires spring.integration.core;
    //requires kafka.clients;

}
