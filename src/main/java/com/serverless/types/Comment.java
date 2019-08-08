package com.serverless.types;

import java.util.Date;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonPOJOBuilder;

import lombok.Builder;
import lombok.NonNull;
import lombok.Value;

@JsonDeserialize(builder = Comment.CommentBuilder.class)
@Builder
@Value
public class Comment {
    @NonNull private String postTitle;
    @NonNull private Date lastUpdated;
    private String comment;

    @JsonPOJOBuilder(withPrefix = "")
    public static final class CommentBuilder {
    }
}
