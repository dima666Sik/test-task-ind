package org.ind.bot.domain.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
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
