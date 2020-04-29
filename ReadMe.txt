Camel Sample Project for Java 
=========================================

The project test the interactions with ActiveMQ/JMS, MySQL DB, File, REST service, translation, routing, etc.
To work with MySQL DB the project expect the DB to be hosted at: mysqlservice:3306 or set env variables DB_SERVER & DB_PORT

To work with ActiveMQ the project expect the ActiveMQ to be hosted at: amqservice:61616 or set env variables AMQ_SERVER & AMQ_PORT

To build this project use

    mvn install

To run the project you can execute the following Maven goal

    mvn exec:java

To deploy the project in OSGi. For example using Apache Karaf.
You can run the following command from its shell:

    osgi:install -s mvn:osa.ora/javatest/1.0.0-SNAPSHOT

For more help see the Apache Camel documentation

    http://camel.apache.org/
