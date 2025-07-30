package org.ind.bot.service;

import lombok.RequiredArgsConstructor;
import org.ind.bot.domain.model.Post;
import org.ind.bot.repository.PostRepository;
import org.springframework.stereotype.Service;

import java.util.NoSuchElementException;

@Service
@RequiredArgsConstructor
public class PostServiceImpl implements PostService {
    // This can be external datasource
    private final PostRepository postRepository;
    @Override
    public Post getPostById(Long id) {
        return postRepository.getPostById(id);
    }
}
