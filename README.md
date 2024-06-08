## Project Motivation

Shortly before the last year of studying starts, many
students are thinking about what their bachelor thesis
will be. Regardless of thematic or context, the amount of literature that one must go through is large. All more
reason to get a little help from an AI-friend! In this project, I leveraged the available transformer models that open ai has to
offer, more specifically the model gpt-3.5-turbo-0125 (mostly because it has very low cost while still being decently performant)
to build myself an AI-assistant. It can provide me useful information related to very specific topics in scientific articles which
I provide to the application.

For this RAG-application, I made use of the very new feature that the spring framework provides, namely Spring AI. It comes
with a very handy toolset of methods and interfaces for working with chat models, text-to-image models, embedding models
and vector databases. I find it exciting to be able to write machine-learning applications within the spring ecosystem.

## How to run this project






## Interpretation and Validation

To test my application against different types of input queries, I provided
below parameters that you can see in the screenshots. One thing I needed
to do was to delete all records in the vector store database after each run,
because all configuration was set after the application context
has been loaded but before the application has completed its startup process.

What I did not change was the input question, to have a meaningful result to benchmark against:

- input question: can you tell me a fact about smart homes?


![first_run.png](first_run.png)
img 1: first run 

The vectorSimilarity property is the output of the service.

for my second run, I changed the defaultChunkSize-parameter to see
if a larger chunks size resulted in better capturing the context and
thus providing a more accurate answer, and indeed we can see that
the cosine similarity in the second run is much higher than in
the first one:

![second_run.png](second_run.png)

for the third run, I changed once again a parameter but this time I chose
to change the minChunkLengthToEmbed-parameter to a much higher value. My expectation
was that accuracy would decrease drastically, because much less tokens
were set to be embedded, thus possibly excluding important pieces of
information. But as the third screenshot shows, cosine similarity was still pretty high:

![third_run.png](third_run.png)

this is likely due to the fact that, while more information overall was excluded,
possible noise or less meaningful information was filtered out, resulting in a decent similarity
between vectors.

