package org.ind.telegram.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.ind.converter.exception.FileWritingException;
import org.ind.converter.service.TextConverter;
import org.ind.telegram.util.RandomGenerator;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.*;

@Service
@Log4j2
@RequiredArgsConstructor
public class HugoService {
    @Value("${hugo.path-to-content-directory}")
    private String pathToContentDirectory;
    private final TextConverter textConverter;
    private static final int MIN_GEN_VALUE = 0;
    private static final int MAX_GEN_VALUE = 200;
    public void handleNewPost(String chatTitle, String text) {
        log.info("New post from {}: {}", chatTitle, text);
        String convertedText = textConverter.convert(text);
        log.info("Converted text: {}", convertedText);
        try (Writer writer = new FileWriter(pathToContentDirectory + "/new-post" + RandomGenerator.generate(MIN_GEN_VALUE, MAX_GEN_VALUE) + ".md");
             BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            bufferedWriter.write(convertedText);
            log.info("The post was written into a file!");
        } catch (IOException e) {
            throw new FileWritingException("The exception happen while writing into a file", e);
        }
    }
}
