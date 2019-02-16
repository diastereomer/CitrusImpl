package com.peitron.citrusimpl;

import com.consol.citrus.annotations.CitrusTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;

import javax.sql.DataSource;

public class DatabaseTest extends CitrusTestBase {

    @Autowired
    private DataSource dataSource;

    @Test
    @CitrusTest
    public void sendSubmissionUpdate() {
        purgeQueues().queueNames("ActiveMQ.DLQ", "request");
        send(jmsEndpoint("um_dw_inbound")).payload(new ClassPathResource("SOAPtest1.xml"));
    }

    @Test
    @CitrusTest
    public void validRequestsShouldNotBeRoutedToDLQ() {
        purgeQueues().queueNames("ActiveMQ.DLQ", "request");
        sql(dataSource).statement("delete from Policy");
        variable("bindDate", "2017-02-15T14:51:51-07:00");
        variable("number", "12");
        send(jmsEndpoint("request")).payload(new ClassPathResource("soap_sample_message.xml"));
        receiveTimeout(jmsEndpoint("ActiveMQ.DLQ"));
        query(dataSource).statement("select count(*) cnt from Policy").validate("cnt", "1");
    }

    @Test
    @CitrusTest
    public void policyWithNullNumberHasToBeWrittenToDatabase() {
        purgeQueues().queueNames("ActiveMQ.DLQ", "request");
        sql(dataSource).statement("delete from Policy");
        variable("bindDate", "2017-02-15T14:51:51-07:00");
        variable("number", "");
        send(jmsEndpoint("request")).payload(new ClassPathResource("soap_sample_message.xml"));
        receiveTimeout(jmsEndpoint("ActiveMQ.DLQ"));
        query(dataSource).statement("select count(*) cnt from Policy").validate("cnt", "1");
    }

    @Test
    @CitrusTest
    public void failingRequestsShouldBeRoutedToDLQ() {
        purgeQueues().queueNames("ActiveMQ.DLQ", "request");
        variable("bindDate", "unknownDate");
        variable("number", "12");
        send(jmsEndpoint("request")).payload(new ClassPathResource("soap_sample_message.xml"));
        receive(jmsEndpoint("ActiveMQ.DLQ")).payload(new ClassPathResource("soap_sample_message.xml"));
    }

}
