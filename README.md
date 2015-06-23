
# spray-finagle-elasticsearch


https://github.com/jrudolph/sbt-dependency-graph

http://www.scala-sbt.org/0.13/docs/Library-Management.html#Exclude+Transitive+Dependencies

http://blog.jaceklaskowski.pl/2014/11/29/sbt-dependency-graph-for-easier-dependency-management-in-sbt.html

> what-depends-on io.dropwizard.metrics metrics-core 3.1.1
[info] io.dropwizard.metrics:metrics-core:3.1.1
[info]   +-com.github.vonnagy:service-container-metrics-reporting_2.11:1.0.1 [S]
[info]   | +-activator-service-container-tutorial:activator-service-container-tutorial:1.0.1 [S]
[info]   | 
[info]   +-com.github.vonnagy:service-container_2.11:1.0.1 [S]
[info]   | +-activator-service-container-tutorial:activator-service-container-tutorial:1.0.1 [S]
[info]   | +-com.github.vonnagy:service-container-metrics-reporting_2.11:1.0.1 [S]
[info]   |   +-activator-service-container-tutorial:activator-service-container-tutorial:1.0.1 [S]
[info]   |   
[info]   +-io.dropwizard.metrics:metrics-jvm:3.1.1
[info]     +-com.github.vonnagy:service-container-metrics-reporting_2.11:1.0.1 [S]
[info]     | +-activator-service-container-tutorial:activator-service-container-tutorial:1.0.1 [S]
[info]     | 
[info]     +-com.github.vonnagy:service-container_2.11:1.0.1 [S]
[info]       +-activator-service-container-tutorial:activator-service-container-tutorial:1.0.1 [S]
[info]       +-com.github.vonnagy:service-container-metrics-reporting_2.11:1.0.1 [S]
[info]         +-activator-service-container-tutorial:activator-service-container-tutorial:1.0.1 [S]
[info]         
[success] Total time: 0 s, completed 23/06/2015 08:50:12 AM
> what-depends-on com.codahale.metrics metrics-core 3.0.1
[info] com.codahale.metrics:metrics-core:3.0.1
[info]   +-com.github.jjagged:metrics-statsd:1.0.0
[info]     +-com.github.vonnagy:service-container-metrics-reporting_2.11:1.0.1 [S]
[info]       +-activator-service-container-tutorial:activator-service-container-tutorial:1.0.1 [S]
[info]       
[success] Total time: 0 s, completed 23/06/2015 08:50:15 AM
>