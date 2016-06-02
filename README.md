# SmartCityApp
An android app created as a part of a college project. It is the UI for a crowd sourced distributed system that.


The app allows users to register usign their email IDs and rate roads.


Through the app, the user will be able to choose any road and give it a rating 0 - 10 (1 is worst and 10 is best).


This rating will reach the servers and Hbase will store it in a distributed database using the Hadoop HDFS.


The resulting Big Data database will then be processed in a distributed manner to answer any queries like – list the top 10 roads with the wost average rating and others.
