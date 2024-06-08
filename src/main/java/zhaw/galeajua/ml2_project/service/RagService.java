package zhaw.galeajua.ml2_project.service;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.chat.model.ChatModel;
import org.springframework.ai.chat.messages.Message;
import org.springframework.ai.chat.messages.UserMessage;
import org.springframework.ai.chat.model.ChatResponse;
import org.springframework.ai.chat.prompt.Prompt;
import org.springframework.ai.chat.prompt.SystemPromptTemplate;
import org.springframework.ai.document.Document;
import org.springframework.ai.embedding.EmbeddingModel;
import org.springframework.ai.vectorstore.SearchRequest;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.JdbcUpdateAffectedIncorrectNumberOfRowsException;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Service;
import zhaw.galeajua.ml2_project.config.EvalRunArgsConfig;
import zhaw.galeajua.ml2_project.controller.RagController;
import zhaw.galeajua.ml2_project.dto.ResponseDto;
import zhaw.galeajua.ml2_project.dto.RunEvalDto;
import zhaw.galeajua.ml2_project.model.VectorSimilarity;
import zhaw.galeajua.ml2_project.util.SimilarityUtil;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class RagService {

    private static final Logger logger = LoggerFactory.getLogger(RagService.class);

    @Value("classpath:/prompts/spring-boot-reference.st")
    private Resource systemPrompt;

    private final ChatModel model;
    private final VectorStore store;
    private final EmbeddingModel embeddingModel;
    private final EvalRunArgsConfig evalRunArgsConfig;
    private final JdbcClient jdbcClient;

    public RagService(ChatModel model, VectorStore store, EmbeddingModel embeddingModel, EvalRunArgsConfig evalRunArgsConfig, JdbcClient jdbcClient) {
        this.model = model;
        this.store = store;
        this.embeddingModel = embeddingModel;
        this.evalRunArgsConfig = evalRunArgsConfig;
        this.jdbcClient = jdbcClient;
    }

    public ResponseDto retrieve(String message) throws IOException {
        SearchRequest request = SearchRequest.query(message).withTopK(evalRunArgsConfig.getTopK());
        List<Document> docs = store.similaritySearch(request);
        Message systemMessage = getSystemMessage(docs);
        UserMessage userMessage = new UserMessage(message);
        Prompt prompt = new Prompt(List.of(systemMessage, userMessage));
        // call the chat model with the prompt
        ChatResponse chatResponse = model.call(prompt);

        String response = chatResponse.getResult().getOutput().getContent();
        List<Double> queryEmbeddings = embeddingModel.embed(message);
        List<Double> responseEmbeddings = embeddingModel.embed(response);
        VectorSimilarity evalResponse = SimilarityUtil.getCosineSimilarity(queryEmbeddings.toArray(new Double[0]), responseEmbeddings.toArray(new Double[0]));

        RunEvalDto runEvalDto = new RunEvalDto.Builder().id(UUID.randomUUID().toString())
                .defaultChunkSize(evalRunArgsConfig.getDefaultChunkSize())
                .minChunkSizeChars(evalRunArgsConfig.getMinChunkSizeChars())
                .minChunkLengthToEmbed(evalRunArgsConfig.getMinChunkLengthToEmbed())
                .maxNumChunks(evalRunArgsConfig.getMaxNumChunks())
                .keepSeparator(evalRunArgsConfig.isKeepSeparator())
                .vectorSimilarity(new VectorSimilarity(evalResponse.getCosineSimilarity(), evalResponse.getInterpretation())).build();

        storeResult(runEvalDto);

        int rowsDeleted = jdbcClient.sql("delete from vector_store").update();
        if (rowsDeleted > 0) {
            logger.info("successfully deleted all table records for new parameterized run");
            return new ResponseDto(response, evalResponse);
        }
        throw new IOException("something went wrong while attempting to delete table records");
    }

    private Message getSystemMessage(List<Document> similarDocuments) {
        String documents = similarDocuments.stream().map(Document::getContent).collect(Collectors.joining("\n"));
        SystemPromptTemplate systemPromptTemplate = new SystemPromptTemplate(systemPrompt);
        return systemPromptTemplate.createMessage(Map.of("documents", documents));
    }

    private void storeResult(RunEvalDto runEvalDto) throws IOException {
        ObjectMapper objectMapper = new ObjectMapper();
        File file = new File(runEvalDto.getId() + "_run_results.json");
        objectMapper.writeValue(file, runEvalDto);
    }
}
