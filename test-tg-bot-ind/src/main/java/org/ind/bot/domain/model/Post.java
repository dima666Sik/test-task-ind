package org.ind.bot.domain.model;

import lombok.*;

@Data
@Builder
public class Post {
    private Long id;
    private String title;
    private String summary;
    private String content;
    private String author;
    private String tags;
    private String sourceUrl;
}
