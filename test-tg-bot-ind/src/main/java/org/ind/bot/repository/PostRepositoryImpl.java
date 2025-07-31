package org.ind.bot.repository;

import org.ind.bot.domain.model.Post;
import org.springframework.stereotype.Repository;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@Repository
public class PostRepositoryImpl implements PostRepository {
    private final Map<Long, Post> postStorage = Map.of(
        1L, Post.builder()
            .id(1L)
            .title("Journey to Mars")
            .summary("Exploring the possibilities of Mars colonization")
            .content("This article dives deep into the technological, biological, and logistical challenges of building a human colony on Mars.")
            .author("Elena Starsky")
            .tags("space, mars, colonization")
            .sourceUrl("https://space.example.com/journey-to-mars")
            .build(),

        2L, Post.builder()
            .id(2L)
            .title("The Mystery of Black Holes")
            .summary("Understanding the universe's most enigmatic objects")
            .content("Black holes are regions of spacetime where gravity is so intense that nothing can escape. This article explains the science and myths behind them.")
            .author("Neil Nova")
            .tags("space, black holes, astrophysics")
            .sourceUrl("https://space.example.com/black-holes")
            .build()
    );

    @Override
    public Post getPostById(Long id) {
        if (!postStorage.containsKey(id)) {
            throw new NoSuchElementException("Post with ID " + id + " not found");
        }
        return postStorage.get(id);
    }

}
