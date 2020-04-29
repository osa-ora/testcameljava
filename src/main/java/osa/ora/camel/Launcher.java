package osa.ora.camel;

import java.util.Optional;

import javax.jms.ConnectionFactory;
import javax.sql.DataSource;

import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.impl.DefaultCamelContext;
import org.apache.camel.main.Main;

import osa.ora.camel.routes.*;

import javax.jms.ConnectionFactory;
import org.apache.activemq.ActiveMQConnectionFactory;
import org.apache.camel.CamelContext;
import org.apache.camel.component.jms.JmsComponent;
import org.apache.camel.builder.RouteBuilder;
import com.mysql.jdbc.jdbc2.optional.MysqlDataSource;


public class Launcher {
    /**
     * A main() so we can easily run these routing rules in our IDE
     */
    public static void main(String... args) throws Exception {
        Main main = new Main();
        
        //This is for DB datasource binding
        // bind dataSource into the registery
        //main.bind("myDataSource", setupDataSource("test","root","passw0rd"));
        //main.addRouteBuilder(new CamelDBRoute());

        main.bind("myDataSource2", setupDataSource("sampledb","root","passw0rd"));
        main.addRouteBuilder(new CamelRestDBRoute());
               
        //main.addRouteBuilder(new CamelRoute());
        //main.addRouteBuilder(new CamelRestRoute());
        main.run(args);
        //AMQ configurations moved to inside the route 
        /*CamelContext ctx = new DefaultCamelContext();
      	configure jms component        
        ConnectionFactory connectionFactory = new ActiveMQConnectionFactory(setupAMQSource());
        ctx.addComponent("jms", JmsComponent.jmsComponentAutoAcknowledge(connectionFactory));
        try {
            //ctx.addRoutes(new CamelRoute());
            ctx.addRoutes(new CamelRestRoute());
            ctx.start();
            Thread.sleep(5 * 60 * 1000);
            ctx.stop();
        }
        catch (Exception e) {
            e.printStackTrace();
        }*/

    }

	static DataSource setupDataSource(String schema, String user, String password) {
		MysqlDataSource ds = new MysqlDataSource();
		//jdbc:mysql://localhost:3306/test
		ds.setURL("jdbc:mysql://" + Optional.ofNullable(System.getenv("DB_SERVER")).orElse("mysqlservice") + ":"
				+ Optional.ofNullable(System.getenv("DB_PORT")).orElse("3306") + "/" + schema);
		ds.setUser(user);
		ds.setPassword(password);
		System.out.println("Will connect to DB: "+ds.getURL());
		return ds;
	}
	public static String setupAMQSource() {
		//tcp://localhost:61616
		String url= "tcp://" + Optional.ofNullable(System.getenv("AMQ_SERVER")).orElse("amqservice") + ":"
				+ Optional.ofNullable(System.getenv("AMQ_PORT")).orElse("61616");
		System.out.println("Will connect to AMQ: "+url);
		return url;
	}

	public static int getListenPort() {
		String port= Optional.ofNullable(System.getenv("PORT")).orElse("8081");
		System.out.println("Will use listen port: "+port);
		return Integer.parseInt(port);
	}
}
