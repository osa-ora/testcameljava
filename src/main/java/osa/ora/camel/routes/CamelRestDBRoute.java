package osa.ora.camel.routes;

import org.apache.camel.Exchange;
import org.apache.camel.LoggingLevel;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.dataformat.JsonLibrary;
import org.apache.camel.model.rest.RestBindingMode;

import osa.ora.camel.beans.Corona;

public class CamelRestDBRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

		System.out.println("Before start ...");
		//route with:
		//1. bind rest end point to localhost on specific port
		//2. expose end point /countries/{country} & /load/{country}
		//3. Call REST service using ?bridgeEndpoint=true to propagate the /countries/{country} path
		//4. Handle errors and return user friendly error message
		//5. Convert the response from JSON to Object
		//6. Save the response from the object into DB
		//7. Load last enty from the DB when using /load/{country}
		restConfiguration().component("jetty").host("localhost").port(8080).bindingMode(RestBindingMode.auto);
		//rest("/countries/").get().consumes(MediaType.APPLICATION_JSON).produces(MediaType.APPLICATION_JSON)
	     //   .to("direct:newCall");	
		from("rest:get:/countries/{country}")
		.log("will call ${header.country}")
		.to("direct:rest");
		
		from("rest:get:/load/{country}")
		.log("will call ${header.country}")
		.to("direct:dbload");
		
		from("direct:rest")
		.setHeader(Exchange.HTTP_METHOD, constant("GET")) 
		.setHeader(Exchange.CONTENT_TYPE, constant("application/json"))
		.setHeader(Exchange.HTTP_URI, constant("https://coronavirus-19-api.herokuapp.com")) 
		//.setHeader(Exchange.HTTP_PATH, simple("/countries/${header.country}"))
		.doTry()
			.to("jetty:https://coronavirus-19-api.herokuapp.com?bridgeEndpoint=true")
			.log("Receiving respnse ${body}")
			.to("direct:dbstroe")
	    .doCatch(Exception.class)
	    	.setBody(simple("Failed to get response for ${header.country}"))
        	.end();

		
		from("direct:dbstroe")
		.unmarshal().json(JsonLibrary.Jackson, Corona.class)
		.process(exchange -> {
            Corona response = exchange.getIn().getBody(Corona.class);
            exchange.getIn().setHeader("cases", ""+response.getCases());
            exchange.getIn().setHeader("deaths", ""+response.getDeaths());
            exchange.getIn().setHeader("data", ""+response);
            System.out.println("Response:"+response);
		})
		.log("will store for country ${header.country} body: ${body}")
		.log(LoggingLevel.ERROR, "Body : ${body}")  
		.log("will store for country ${header.cases} body: ${header.deaths}")
		.setBody(simple("INSERT INTO corona (country, cases, deaths,data_time) "
				+ "VALUES ('${header.country}',${header.cases},${header.deaths},"
				+ "CURRENT_TIMESTAMP())"))
		.log(body().toString())
        .to("jdbc:myDataSource2").
        setBody(simple("${header.data}"));
        //.to("jdbc:myDataSource?outputClass=osa.ora.camel.beans.Account")
        //.to("activemq:queue:test");
		
		from("direct:dbload")
		.setBody(simple("select country ,cases ,deaths from corona "
				+ "where country='${header.country}' ORDER BY id DESC LIMIT 1"))
        //.to("jdbc:myDataSource")
        .to("jdbc:myDataSource2?outputClass=osa.ora.camel.beans.Corona")
        //.log("Receiving response ${body}")
        //.process(new RowProcessor())
        .log("Receiving response ${body}");				
		
	}

}
