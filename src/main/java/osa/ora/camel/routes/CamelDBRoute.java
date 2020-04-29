package osa.ora.camel.routes;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.model.rest.RestBindingMode;

import osa.ora.camel.beans.Account;

public class CamelDBRoute extends RouteBuilder {

	@Override
	public void configure() throws Exception {

 		System.out.println("Before start ...");
		//route with:
		//1. bind rest end point to localhost on specific port
		//2. expose end point /account/{id}
		//3. Use the path parameter to fill the query parameter
		//4. Execute SQL query against the DB
 		//5. Supply the query response class type or keep it open and use processor to map it
 		//6. Log the response 
 		//7. Use the processor to manipulate in and out
		restConfiguration().component("jetty").host("localhost").port(8080).bindingMode(RestBindingMode.json);
		//rest("/countries/").get().consumes(MediaType.APPLICATION_JSON).produces(MediaType.APPLICATION_JSON)
	     //   .to("direct:newCall");	
		from("rest:get:/account/{id}")
		//.setBody(simple("${header.id}"))
		//.log(body().toString())
		.setBody(simple("select * from test.accounts where id=${header.id}"))
        //.to("jdbc:myDataSource")
        .to("jdbc:myDataSource?outputClass=osa.ora.camel.beans.Account")
        //.log("Receiving response ${body}")
        //.process(new RowProcessor())
        .log("Receiving response ${body}");				
	}

}
class RowProcessor implements Processor {
 
    @SuppressWarnings("unchecked")
	public void process(Exchange exchange) {
    	try {
    		ArrayList<Map<String, String>> dataList = (ArrayList<Map<String, String>>) exchange.getIn()
					.getBody();
			List<Account> employees = new ArrayList<Account>();
			System.out.println(dataList);
			for (Map<String, String> data : dataList) {
				Account employee = new Account();
				employee.setId(Integer.parseInt(data.get("id")));
				employee.setName(data.get("name"));
				employee.setAddress(data.get("address"));
				employee.setPhone(data.get("phone"));
				employee.setActive(Integer.parseInt(data.get("name")));
				employees.add(employee);
			}
			exchange.getIn().setBody(employees);	
       /* Map<String, Object> row = exchange.getIn().getBody(Map.class);
        System.out.println("Processing " + row);
        Account account = new Account();
         
        account.setName((String) row.get("name"));
        account.setPhone((String) row.get("phone"));
        account.setAddress((String) row.get("address"));
        account.setActive((Boolean) row.get("active"));
        account.setId((Integer) row.get("id"));
        exchange.getIn().setBody(account);
         
        exchange.getOut().setBody(account);*/
    	} catch(Throwable t) {
    		t.printStackTrace();
    	}
    }
 
}
