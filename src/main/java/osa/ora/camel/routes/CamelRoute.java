package osa.ora.camel.routes;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;

public class CamelRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {
		//route with:
		//1. Start by reading/listening to specific file filters
		// and move the processed files into a specific folder
		//2. Split content by tokenize using \n 
		//3. Use Choice and when and filter it string content
		//4. Call different REST end points services
		//5. Submit messages to message queue
		//6. Listen and consume messages from message queue
		//7. Create file and append to it
		
        //if(getContext().getComponent("jms")==null) {
    		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory("tcp://localhost:61616");
    		getContext().addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
		//}
        System.out.println("Before start ...");
		from("file:d:/dev/test2?move=.done&antInclude=*.txt")
		.split().tokenize("\n")
		.log("will call ${body}")
		.setHeader(Exchange.HTTP_METHOD, constant("GET")) 
		.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
		//.setHeader(Exchange.HTTP_URI, constant("https://coronavirus-19-api.herokuapp.com")) 
		//.setHeader(Exchange.HTTP_PATH, simple("/countries/${body}"))
	    //.setHeader(Exchange.HTTP_QUERY, simple("userid=${header.userName}"))
		.choice().
        when(body().contains("Egypt"))
		.to("direct:egypt").
        when(body().contains("Italy"))
		.to("direct:italy").
        when(body().contains("China"))
		.to("direct:china").
        otherwise()
		.log("Not identitified");
		
		from("direct:egypt")
		.to("jetty:https://coronavirus-19-api.herokuapp.com/countries/Egypt")
		.log(body().toString())
		.log("Receiving respnse ${body}")
		.to("direct:store");
		
		from("direct:italy")
		.to("jetty:https://coronavirus-19-api.herokuapp.com/countries/Italy")
		.log(body().toString())
		.log("Receiving respnse ${body}")
		.to("direct:store");
		
		from("direct:china")
		.to("jetty:https://coronavirus-19-api.herokuapp.com/countries/China")
		.log(body().toString())
		.log("Receiving respnse ${body}")
		.to("direct:store");
		
		from("direct:store")
		.log("will store in queue ${body}")
        .to("jms:queue:test");
		
		from("jms:queue:test")
		.log("will consume from the queue ${body}")
		.to("file:d:/dev/test2?fileExist=Append&fileName=osa${date:now:yyyyMMdd}.t2t");
	}

}
