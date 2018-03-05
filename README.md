# JPM-NotiPro - A simple Sales Notification Processing application #

## The problem ##
Implement a small message processing application that satisfies the below requirements for
processing sales notification messages. You should assume that an external company will be sending
you the input messages, but for the purposes of this exercise you are free to define the interfaces.

### Expectations ###
Treat this exercise as if it was a task you were implementing as part of a normal working day. In your submission you are expected to include everything you would commit to source control before signing off the task as production ready.
* No database or UI is required
* You can assume the code will only ever be executed in a single threaded environment
* Minimise the number of external jar dependencies your code has. We would expect a maximum of 1 or two would be required.
* All data to be in memory.
* Output format to be plain text, printed out to the console.
* Create more sample data as needed.
* We would expect you to spend somewhere in the region of about 3 hours on this exercise.

### Processing requirements ###
* All sales must be recorded
* All messages must be processed
* After every 10th message received your application should log a report detailing the number of sales of each product and their total value.
* After 50 messages your application should log that it is pausing, stop accepting new messages and log a report of the adjustments that have been made to each sale type while the application was running.

### Sales and Messages ###
* A sale has a product type field and a value – you should choose sensible types for these.
* Any number of different product types can be expected. There is no fixed set.
* A message notifying you of a sale could be one of the following types
  * **Message Type 1** – contains the details of 1 sale E.g apple at 10p
  * **Message Type 2** – contains the details of a sale and the number of occurrences of that sale. E.g 20 sales of apples at 10p each.
  * **Message Type 3** – contains the details of a sale and an adjustment operation to be applied to all stored sales of this product type. Operations can be add, subtract, or multiply e.g Add 20p apples would instruct your application to add 20p to each sale of apples you have recorded.

## Solution ##
Designed a simple java application which reads a file `(*.txt in this case)` line by line and records and processes each notification received. Tried to emulate an MVC call, so kept the core architecture similar to an MVC application. Used lambda expressions/statements inorder to keep the code short and legible.

### Algorithm ###
* File is read line by line and the notification or line content is passed onto a controller method.
* The controller increments a counter and passes on the notification to the manager, along with instructions after every 10th notification and after 50 notifications have been received.
* The manager the passes the notification to a parser to parse the notification and get the details of the sale in object notation.
* The Sale object is then recorded in a particular product which is then in turn stored in a HashMap. While soring the sale onto a product the necessary processing for calculating the totas are done.
* After every 10 messages and after 50 messages, when the controller send the necessary indication to the manager, it then inturn invokes the report logger to print a report onto the console. A delay of 1 second is induced after priting ever report.

### Initial design thoughts ###
* The first and most obvious choice was to use the JMS API and have a **consumer** constructed to accept and process the notifications. This would also ensure that no messages are lost when the consumer pauses after 50 notifications as if the **producer** can be made to demand an acknowledgment `(by setting the session's AUTO_ACKNOWLEDGEMENT to false)`, which would make the message to be stored until its expiration time in the queue, which then could be delivered to the consumer when it turns back on. **_--> Tunred down due to external jar dependencies._**
* Create a RESTful WebService client and server where the client sends sales notifications at regular(or irregular if need be) intervals and the server receives, records and processes these notifications **_--> Turned down due to external jar dependencies and time constraint._**
  * Also had the idea of creating a simple spring-boot application but had too many external jar dependencies due to which didn't go ahead with this idea.

### Assumptions ###
* After receiving, recording and processing the 50th notification, the application stops --> treated this as an End Of Business Day scenario, so ideally no more notifications are epxected.
* The product types are mostly common retail items as the plural/singular form of the product types coming in through notifications couldn't all be handled. There wasn't any **reliable** open-source API that I could leverage as well.
* The message types are strictly confirming to the examples provided and any deviations from them would lead to have the message being treated as invalid. Product types are supposed to be sent as a single word, multi-worded types needs to be sent with a word joiner (_ or - etc..)
* Have not created test cases since it involved haveing more external jar dependencies. Would have preferred to use EasyMock (Unitils-JUnit) whcih I find to be quite helpful during development.

## Getting Started ##
You can use Git clone with HTTPS https://github.com/abhi-nair/JPM-NotiPro or downlaod the zip of the checked-in sources. Import into any IDE like IntelliJ, Eclipse and then run the main method of the class -> NotiProApplication.java once the maven build is complete.
* Results will be printed onto the console.
* A log file will be created in the classpath with the default logger level set to INFO

### Pre-requisites ###
 1. [Java](https://docs.oracle.com/javase/8/docs/technotes/guides/install/install_overview.html) version: 8.0 (JDK 1.8, jre8)
 2. [Maven](https://maven.apache.org/install.html) - Build for dependency management
 
# Author #
Abhishek Gopinath Nair
* [LinkedIn](www.linkedin.com/in/AbhishekNair-9891a7082)
* [Email](mailto:36994114+abhi-nair@users.noreply.github.com)
