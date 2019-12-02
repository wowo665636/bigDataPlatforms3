# Part 1 - Design for streaming analytics

##### 1. Select a dataset suitable for streaming analytics for a customer as a running example (thus the basic unit of the data should be a discrete record/event data). Explain the dataset and at least two different analytics for the customer: (i) a streaming analytics which analyzes streaming data from the customer (customerstreamapp) and (ii) a batch analytics which analyzes historical results outputted by the streaming analytics. The explanation should be at a high level to allow us to understand the data and possible analytics so that, later on, you can implement and use them in answering other questions.

The dataset contains daily statistics on trending youtube videos in 2017 in multiple countries. It was found on the following website: https://www.kaggle.com/datasnaek/youtube-new
The dataset is in the form of one csv file per country (where the video became viral), and each entry has the following attributes: 

- video_id, the official youtube id string 
- trending_date, the date at which the video was trending, in the format 
- title, the title of the video
- channel_title, the title of the channel
- category_id, the id of the category (separate files containing category information exist, but they were not used)
- publish_time, the time at which the video was published
- tags, the tags of the video
- views, the amount of views, a numeric value
- likes, the amount of likes, a numeric value
- dislikes, the amount of dislikes, a numeric value
- comment_count, the number of comments, a numeric value
- thumbnail_link, a link to the video thumbnail's image
- comments_disabled, whether the comments were disabled or not, a boolean
- ratings_disabled, whether the ratings were disabled or not, a boolean
- video_error_or_removed, whether the video has been removed or if there is an error with it
- description, the description written by the original poster 

An example of streaming analytics can be to return the ratio of likes to dislikes to explain whether the fame came from the appreciation of the video or the opposite.
In terms of batch analytics, we can have a batch analysis for each month (as each entry contains the date at which the video became viral). In association with the data regarding categories, we could return the proportions of all the categories in one month's trends.  

##### 2. Customers will send data through message brokers/messaging systems which become data stream sources. Discuss and explain the following aspects for the streaming analytics: (i) should the analytics handle keyed or non-keyed data streams for the customer data, and (ii) which types of delivery guarantees should be suitable. 

For this dataset, I believe the analytics should handled keyed streams, each key being the country in which the video became viral. This allows for parallel computing of analytics in all countries. This assumes there will not be analytics that involve using data across multiple countries. 
The type of delivery guarantee that suits best is "at least once". For the type of data that is processed, we would like to get the output of the processing at least once, and there are not risks or drawbacks to risking getting the feedback multiple times in order to ensure we will get it at least once. If we associate to our output the date for which the analysis corresponds to, if we find ourselves sending back the same analysis twice it will not lead to confusion. 

##### 3. Given streaming data from the customer (selected before). Explain the following issues: (i) which types of time should be associated with stream sources for the analytics and be considered in stream processing (if the data sources have no timestamps associated with events, then what would be your solution), and (ii) which types of windows should be developed for the analytics (if no window, then why). Explain these aspects and give examples.

The data is about trending videos in a day. The day has to pass for the data to be generated, so we assume the only time information we can get is the day it became viral but nothing finer like a time of the day. 
Therefore, to get an actual timestamp, we can use the time the event arrived to the platform as the timestamp. 

Windowing can allow us to group events together: we can do it based on the timestamp, or on an attribute. I believe it would be interesting to group events based on their trending date, so the timestamp used for the aggregation is the trending date.
In order to implement that, the best window type is a session window, with variable size and the session interval needs to be established beforehand, depending on over how long the producer releases one day's trending videos. 

##### 4. Explain which performance metrics would be important for the streaming analytics for your customer cases. 

//////////////////////

##### 5. Provide a design of your architecture for the streaming analytics service in which you clarify: customer data sources, mysimbdp message brokers, mysimbdp streaming computing service, customer streaming analytics app, mysimbdp-coredms, and other components, if needed. Explain your choices of technologies for implementing your design and reusability of existing assignment works. Note that the result from customerstreamapp will be sent back to the customer in near real-time. 

The customer produces data through the RabbitMq message broker. Its data source are in the data folder at the root of the project directory. One the other side, MySimbdp's streaming computing service creates a channel for each of its customer and is awaiting for data to be send through the different customer channels. Moreover, it also creates a thread in which the customer streaming analytics app is ran. When data is produced by a customer and mysimbdp message brokers receive it, and the data obtained is passed down to that app, through the app's output stream. Persisting information has not been implemented, but the customer app would have access to mysimbdp-daas in order to save into mysimbdp-coredms the data it's analysing. Finally, when the analysis is done, the customer app prints out its output, and the Mysimbdp streaming computing service catches it by listening to the app's input stream. This service also has a channel for which it is a producer and the customer is the consumer, and it sends through that channel the output of the analysis. 

# Part 2 - Implementation of streaming analytics

##### 1. Explain the implemented structures of the input streaming data and the output result, and the data serialization/deserialization, for the streaming analytics application (customerstreamapp) for customers. 

The input streaming data is byte array, that can be parsed to a json string containing an object with keys being the attributes names listed in the previous section, and their value. 
Once it gets to the client streaming analytics app, it is deserialized into the object "TrendingVideo" (partially only, for the sake of the current analysis).
We use the Flink-RabbitMq connector, give it the information it needs to listen to the appropriate connection (RMQConnectionConfig), the channel name, and we also give it a deserializationSchema that takes care of the deserialization mentioned previously. 

The analysis is rather basic, all it does is find the proportion of likes compared to the amount of "opinions" voiced (sum of like and dislikes), so in terms of object mapping, we go from a TrendingVideo to a Double. 

Finally, we use a sink to connect the stream of data to the output channel that gives it back to the client, and we give it a SerializationSchema that turns the doubles into byte arrays. 

##### 2. Explain the key logic of functions for processing events/records in customerstreamapp in your implementation. 

//////////////////////

##### 3. Run customerstreamapp and show the operation of the customerstreamapp with your test environments. Explain the test environments. Discuss the analytics and its performance observations. 

//////////////////////

##### 4. Present your tests and explain them for the situation in which wrong data is sent from or is within data sources. Report how your implementation deals with that (e.g., exceptions, failures, and decreasing performance). You should test with different error rates. 

//////////////////////

##### 5. Explain parallelism settings in your implementation and test with different (higher) degrees of parallelism. Report the performance and issues you have observed in your testing environments. 

//////////////////////

# Part 3 - Connection

##### 1. If you would like the analytics results to be stored also into mysimbdp-coredms as the final sink, how would you modify the design and implement this (better to use a figure to explain your design). 

![image](/images/1.bmp) ![image](/images/2.bmp)

When the customer stream app outputs its analysis, right before sending it to the rabbitmq channel, we should make a call to our rest api to store the output of the analysis to our database. 

##### 2. Given the output of streaming analytics stored in mysimbdp-coredms for a long time. Explain a batch analytics (see also Part 1, question 1) that could be used to analyze such historical data. How would you implement it? 

Batch analytics could be implemented with a cron job, that would be triggered once a month. 
We can also do it at the request of the customer 
We'd query all analysis that correspond to the month we need. The batch analysis would still be done with Flink. 
##### 3. Assume that the streaming analytics detects a critical condition (e.g., a very high rate of alerts) that should trigger the execution of a batch analytics to analyze historical data. How would you extend your architecture in Part 1 to support this (use a figure to explain your work)?. 

//////////////////////

##### 4. If you want to scale your streaming analytics service for many customers and data, which components would you focus and which techniques you want to use? 

In order to scale for many customers and data, I would focus on the StreamComputingService component, and manage the creation of threads in a better way. Currently, there is a thread for each client created to listen to incoming messages, and for each client another one that sends back the data when the analytics app outputs it. 


//////////////////////

##### 5. Is it possible to achieve end-to-end exactly once delivery in your current implementation? If yes, explain why. If not, what could be conditions and changes to make it happen? If it is impossible to have end-to-end exactly once delivery in your view, explain why. 

//////////////////////
