package com.peitron.citrusimpl;
import com.consol.citrus.dsl.junit.JUnit4CitrusTestDesigner;
import com.consol.citrus.jms.endpoint.JmsEndpoint;
import com.consol.citrus.jms.endpoint.JmsEndpointConfiguration;
import org.springframework.beans.factory.annotation.Autowired;

import javax.jms.ConnectionFactory;

public abstract class CitrusTestBase extends JUnit4CitrusTestDesigner {

    @Autowired
    private ConnectionFactory connectionFactory;

    protected JmsEndpoint jmsEndpoint(String name) {
        JmsEndpointConfiguration configuration = new JmsEndpointConfiguration();
        configuration.setConnectionFactory(connectionFactory);
        configuration.setDestinationName(name);
        return new JmsEndpoint(configuration);
    }

}
