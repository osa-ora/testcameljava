package osa.ora.camel.routes;

import javax.jms.ConnectionFactory;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.Exchange;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.model.rest.RestBindingMode;

import osa.ora.camel.Launcher;

public class CamelRestRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

        //if(getContext().getComponent("activemq")==null) {
    		ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(Launcher.setupAMQSource());
    		getContext().addComponent("activemq", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
		//}	
		System.out.println("Before start ...");
		//route with:
		//1. bind rest end point to localhost on specific port
		//2. expose end point /countries/{country}
		//3. Use the path parameter to fill the body
		//4. Use Choice/When to select the service to call
		//5. Call REST service using ?bridgeEndpoint=true and ?bridgeEndpoint=false
		//   i.e. do propagate the path or not
		//6. Submit body content into message queue
		//7. Submit messages to message queue
		//8. Listen and consume messages from message queue
		//9. Create file and append to it
		restConfiguration().component("jetty").host("localhost").port(Launcher.getListenPort()).bindingMode(RestBindingMode.json);
		//rest("/countries/").get().consumes(MediaType.APPLICATION_JSON).produces(MediaType.APPLICATION_JSON)
	     //   .to("direct:newCall");	
		from("rest:get:/countries/{country}")
		.setBody(simple("${header.country}"))
		  .to("direct:newCall");
		  //.transform().simple("Bye ${header.country}");
	
		from("direct:newCall")
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
		.to("jetty:https://coronavirus-19-api.herokuapp.com?bridgeEndpoint=true")
		.log(body().toString())
		.log("Receiving respnse ${body}")
		.to("direct:store");
		
		from("direct:italy")
		.removeHeaders("CamelHttp*")
		.setHeader(Exchange.HTTP_METHOD, constant("GET")) 
		.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
		.to("jetty:https://coronavirus-19-api.herokuapp.com/countries/Italy?bridgeEndpoint=false")
		.log(body().toString())
		.log("Receiving respnse ${body}")
		.to("direct:store");
		
		from("direct:china")
		.to("jetty:https://coronavirus-19-api.herokuapp.com?bridgeEndpoint=true")
		.log(body().toString())
		.log("Receiving respnse ${body}")
		.to("direct:store");
		
		from("direct:store")
		.log("will store in queue ${body}")
        .to("activemq:queue:test");
		
		from("activemq:queue:test")
		.log("will consume from the queue ${body}")
		.to("file:d:/dev/test2?fileExist=Append&fileName=osa${date:now:yyyyMMdd}.t2t");
		
	}

}
