package zhaw.galeajua.ml2_project.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.stereotype.Component;


@Getter
@Setter
@Component
@ConfigurationProperties(prefix = "modelargs")
public class EvalRunArgsConfig {

    private int defaultChunkSize;
    private int minChunkSizeChars;
    private int minChunkLengthToEmbed;
    private int maxNumChunks;
    private boolean keepSeparator;
    private int numberOfBottomTextLinesToDelete;
    private int pagesPerDocument;
    private int topK;
}
