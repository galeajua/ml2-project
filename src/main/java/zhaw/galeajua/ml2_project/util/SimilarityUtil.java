package zhaw.galeajua.ml2_project.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import zhaw.galeajua.ml2_project.model.VectorSimilarity;
import zhaw.galeajua.ml2_project.service.RagService;

public class SimilarityUtil {

    private static final Logger logger = LoggerFactory.getLogger(RagService.class);

    private static double calculateCosineSimilarity (Double[] vectorA, Double[] vectorB) {
        double dotProduct = 0.0;
        double normA = 0.0;
        double normB = 0.0;
        for (int i = 0; i < vectorA.length; i++) {
            dotProduct += vectorA[i] * vectorB[i];
            normA += Math.pow(vectorA[i], 2);
            normB += Math.pow(vectorB[i], 2);
        }
        return dotProduct / (Math.sqrt(normA) * Math.sqrt(normB));
    }
    public static VectorSimilarity getCosineSimilarity(Double[] vectorA, Double[] vectorB) {
        double simResult = calculateCosineSimilarity(vectorA, vectorB);
        String interpretaton = null;
        if (simResult >= 0.8) {
            interpretaton = "vectors are of high similarity";
        }
        else if (simResult >= 0.5) {
            interpretaton = "vectors are of moderate similarity";
        }
        else {
            interpretaton = "vectors are of low similarity";
        }
        return new VectorSimilarity(simResult, interpretaton);
    }

}
