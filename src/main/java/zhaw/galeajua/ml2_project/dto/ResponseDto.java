package zhaw.galeajua.ml2_project.dto;

import zhaw.galeajua.ml2_project.model.VectorSimilarity;

public class ResponseDto {

    private String message;
    private VectorSimilarity similarity;

    public ResponseDto() {}

    public ResponseDto(String message, VectorSimilarity similarity) {
        this.message = message;
        this.similarity = similarity;
    }

    public String getMessage() {
        return message;
    }

    public VectorSimilarity getSimilarity() {
        return similarity;
    }
}
