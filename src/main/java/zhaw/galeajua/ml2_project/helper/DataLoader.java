package zhaw.galeajua.ml2_project.helper;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.ai.document.Document;
import org.springframework.ai.reader.ExtractedTextFormatter;
import org.springframework.ai.reader.pdf.PagePdfDocumentReader;
import org.springframework.ai.reader.pdf.config.PdfDocumentReaderConfig;
import org.springframework.ai.transformer.splitter.TokenTextSplitter;
import org.springframework.ai.vectorstore.VectorStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.core.io.Resource;
import org.springframework.jdbc.core.simple.JdbcClient;
import org.springframework.stereotype.Component;
import zhaw.galeajua.ml2_project.config.EvalRunArgsConfig;

import java.util.*;


@Component
public class DataLoader implements ApplicationRunner {

    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    @Value("classpath:/data/")
    private Resource data;

    private final JdbcClient jdbcClient;

    private final VectorStore vectorStore;
    private final EvalRunArgsConfig evalRunArgsConfig;

    @Value("classpath:/docs/digital_playbook.pdf")
    private Resource doc1;

    @Value("classpath:/docs/anthropomorphic_appearance.pdf")
    private Resource doc2;

    @Value("classpath:/docs/smart_home_tech_adoption.pdf")
    private Resource doc3;

    public DataLoader(JdbcClient jdbcClient, VectorStore vectorStore, EvalRunArgsConfig evalRunArgsConfig) {
        this.vectorStore = vectorStore;
        this.jdbcClient = jdbcClient;
        this.evalRunArgsConfig = evalRunArgsConfig;
    }

    @Override
    public void run(ApplicationArguments args) throws Exception {
        Integer count = jdbcClient.sql("select count(*) from vector_store")
                .query(Integer.class)
                .single();

        logger.info("Current count of the Vector Store: {}", count);
        if (count == 0) {
            logger.info("Loading Spring Boot Reference PDF into Vector Store");
            PdfDocumentReaderConfig config = PdfDocumentReaderConfig.builder()
                    .withPageExtractedTextFormatter(new ExtractedTextFormatter.Builder().withNumberOfBottomTextLinesToDelete(evalRunArgsConfig.getNumberOfBottomTextLinesToDelete())
                            .withNumberOfTopPagesToSkipBeforeDelete(0)
                            .build())
                    .withPagesPerDocument(evalRunArgsConfig.getPagesPerDocument())
                    .build();
            List<Resource> resources = List.of(doc1, doc2, doc3);
            if (resources.stream().allMatch(Resource::exists)) {
                Map<String, PagePdfDocumentReader> docReaderSet = new HashMap<>();
                for (Resource resource : resources) {
                    docReaderSet.putIfAbsent(resource.getFilename(), new PagePdfDocumentReader(resource, config));
                }
                TokenTextSplitter textSplitter = new TokenTextSplitter(evalRunArgsConfig.getDefaultChunkSize(),
                        evalRunArgsConfig.getMinChunkSizeChars(),
                        evalRunArgsConfig.getMinChunkLengthToEmbed(),
                        evalRunArgsConfig.getMaxNumChunks(),
                        evalRunArgsConfig.isKeepSeparator());
                List<Document> combined = new ArrayList<>();
                for (Map.Entry<String, PagePdfDocumentReader> entry : docReaderSet.entrySet()) {
                    logger.info("adding the documents for {} to combined list", entry.getKey());
                    combined.addAll(entry.getValue().get());
                }
                vectorStore.accept(textSplitter.apply(combined));
                logger.info("Application is ready");
            }
            else {
                throw new Exception("one or more docs are not available");
            }
        }
    }
}
