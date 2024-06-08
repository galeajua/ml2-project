package zhaw.galeajua.ml2_project.model;

import zhaw.galeajua.ml2_project.util.SimilarityUtil;

public class VectorSimilarity {

    private double cosineSimilarity;
    private String interpretation;

    public VectorSimilarity() {}

    public VectorSimilarity(double cosineSimilarity, String interpretation) {
        this.cosineSimilarity = cosineSimilarity;
        this.interpretation = interpretation;
    }

    public double getCosineSimilarity() {
        return cosineSimilarity;
    }

    public void setCosineSimilarity(double similarity) {
        this.cosineSimilarity = similarity;
    }

    public String getInterpretation() {
        return interpretation;
    }
}
