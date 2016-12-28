# **SENTIMENT RATING OF TRENDING TOPICS IN TWITTER**
## Sentiment rating of top 5 trending topics in twitter using JAVA SE8, Twitter4j api and Bing Text Analytics api.

This program uses preview version of Bing Text Analytics api, which limits the number of calls to 5000 per month. 
So to use this, register your microsoft account for api preview and get the key.

## Output Metrics:
  1. Sentiment score. range: 0-100 
  
## Output Inference:
  2. Low score indicate negative sentiment.
  3. High score indicate positive sentiment. 

## How to Use it:
  1. Change the xxxxxx in TweetExtract.java class to your Twitter4j keys.
  2. Change the xxxxxx in SentimentExtract.java class to your microsoft api preview key.
  3. Change the geographic location in geoloc. It is defaultly set to India.
  4. That's it. Compile and run the main class (ie: TweetExtract.java).

## How program works:
### Gathering and formatting data:
  1. It builds the configuration and gets a twitter instance from which the trending topics are retrieved.
  2. For every topic, 50 tweets are downloaded and each tweet undergoes a cleaning process. The cleaning process consists of removing URLs, #text, RT text and special characters.
  3. All the 50 cleaned tweets are combined to a huge string (like a paragraph).
  4. The paragraph is converted to a specific JSON object which is then sent for sentiment analysis.

### Receive response and analyse:
  1. Response from api is an http entity, which is converted to a string.
  2. The String is parsed using JSON parser to create a JSON object of the response.
  3. From the JSON object the score is retrieved.

For every trending topic, tweet retrieval is performed 5 times and scores of each attempt is calculated and the average of 5 attempts is posted as the final sentiment score. This is done for attaining better accuracy and consistency.

Disclaimer : no copyright infringement intended.

