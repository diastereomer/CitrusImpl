package com.peitron.citrusimpl;

import com.consol.citrus.annotations.CitrusTest;
import org.junit.Test;
import org.springframework.core.io.ClassPathResource;

public class JpaTest extends CitrusTestBase {

    @Test
    @CitrusTest
    public void sendSubmissionUpdate() {
        purgeQueues().queueNames("ActiveMQ.DLQ", "savePolicy");
        send(jmsEndpoint("savePolicy")).payload(new ClassPathResource("SOAPTest2.xml"));
    }
}
