package org.ind.bot.repository;

import org.ind.bot.domain.model.Post;

public interface PostRepository {
    Post getPostById(Long id);

}
