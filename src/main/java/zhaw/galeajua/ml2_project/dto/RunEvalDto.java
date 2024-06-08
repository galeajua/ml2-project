package zhaw.galeajua.ml2_project.dto;

import zhaw.galeajua.ml2_project.model.VectorSimilarity;

public class RunEvalDto {

    private String id;
    private int defaultChunkSize;
    private int minChunkSizeChars;
    private int minChunkLengthToEmbed;
    private int maxNumChunks;
    private boolean keepSeparator;
    private VectorSimilarity vectorSimilarity;



    public void setId(String id) {
        this.id = id;
    }

    public void setDefaultChunkSize(int defaultChunkSize) {
        this.defaultChunkSize = defaultChunkSize;
    }

    public void setMinChunkSizeChars(int minChunkSizeChars) {
        this.minChunkSizeChars = minChunkSizeChars;
    }

    public void setMinChunkLengthToEmbed(int minChunkLengthToEmbed) {
        this.minChunkLengthToEmbed = minChunkLengthToEmbed;
    }

    public void setMaxNumChunks(int maxNumChunks) {
        this.maxNumChunks = maxNumChunks;
    }

    public void setKeepSeparator(boolean keepSeparator) {
        this.keepSeparator = keepSeparator;
    }

    public void setVectorSimilarity(VectorSimilarity vectorSimilarity) {
        this.vectorSimilarity = vectorSimilarity;
    }

    public String getId() {
        return id;
    }

    public int getDefaultChunkSize() {
        return defaultChunkSize;
    }

    public int getMinChunkSizeChars() {
        return minChunkSizeChars;
    }

    public int getMinChunkLengthToEmbed() {
        return minChunkLengthToEmbed;
    }

    public int getMaxNumChunks() {
        return maxNumChunks;
    }

    public boolean isKeepSeparator() {
        return keepSeparator;
    }

    public VectorSimilarity getVectorSimilarity() {
        return vectorSimilarity;
    }

    public RunEvalDto (String id, int defaultChunkSize, int minChunkSizeChars, int minChunkLengthToEmbed, int maxNumChunks,
                                   boolean keepSeparator, VectorSimilarity vectorSimilarity) {
        this.id = id;
        this.defaultChunkSize = defaultChunkSize;
        this.minChunkSizeChars = minChunkSizeChars;
        this.minChunkLengthToEmbed = minChunkLengthToEmbed;
        this.maxNumChunks = maxNumChunks;
        this.keepSeparator = keepSeparator;
        this.vectorSimilarity = vectorSimilarity;
    }

    public RunEvalDto() {}

    private RunEvalDto(Builder builder) {
        this.id = builder.id;
        this.defaultChunkSize = builder.defaultChunkSize;
        this.minChunkSizeChars = builder.minChunkSizeChars;
        this.minChunkLengthToEmbed = builder.minChunkLengthToEmbed;
        this.maxNumChunks = builder.maxNumChunks;
        this.keepSeparator = builder.keepSeparator;
        this.vectorSimilarity = builder.vectorSimilarity;
    }

    public static class Builder {
        private String id;
        private int defaultChunkSize;
        private int minChunkSizeChars;
        private int minChunkLengthToEmbed;
        private int maxNumChunks;
        private boolean keepSeparator;
        private VectorSimilarity vectorSimilarity;

        public Builder id(String id) {
            this.id = id;
            return this;
        }

        public Builder defaultChunkSize(int defaultChunkSize) {
            this.defaultChunkSize = defaultChunkSize;
            return this;
        }

        public Builder minChunkSizeChars(int minChunkSizeChars) {
            this.minChunkSizeChars = minChunkSizeChars;
            return this;
        }

        public Builder minChunkLengthToEmbed(int minChunkLengthToEmbed) {
            this.minChunkLengthToEmbed = minChunkLengthToEmbed;
            return this;
        }

        public Builder maxNumChunks(int maxNumChunks) {
            this.maxNumChunks = maxNumChunks;
            return this;
        }

        public Builder keepSeparator(boolean keepSeparator) {
            this.keepSeparator = keepSeparator;
            return this;
        }

        public Builder vectorSimilarity(VectorSimilarity vectorSimilarity) {
            this.vectorSimilarity = vectorSimilarity;
            return this;
        }

        public RunEvalDto build() {
            return new RunEvalDto(this);
        }
    }
}
