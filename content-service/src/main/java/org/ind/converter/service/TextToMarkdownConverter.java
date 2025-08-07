package org.ind.converter.service;

import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class TextToMarkdownConverter implements TextConverter {
    @Override
    public String convert(String text) {
        Set<String> setOfKeyTopicsPost = new HashSet<>(List.of(
            "title", "summary", "content", "author", "tags", "source"
        ));
        String[] arrayOfContent = text.split("\n");
        StringBuilder convertedText = new StringBuilder();
        /*
         * TODO
         * here I'm minus 1 day to have opportunity to upload the post in hugo
         * (because it doesn't work with future post... This place in code can be improved, it's just a plug now)
         */
        convertedText.append("---").append("\n")
            .append("title: ").append("\"").append(arrayOfContent[0].split(":")[1].trim()).append("\"").append("\n")
            .append("date: ").append(LocalDate.now().minusDays(1)).append("\n")
            .append("draft: ").append(false).append("\n")
            .append("---").append("\n\n");

        for (String line : arrayOfContent) {
            String[] arrayOfTitleAndContent = line.split(":");
            if (setOfKeyTopicsPost.contains(arrayOfTitleAndContent[0].trim().toLowerCase())) {
                convertedText.append(arrayOfTitleAndContent[0].trim()).append(": ").append(arrayOfTitleAndContent[1].trim()).append("\n");
            } else {
                convertedText.append(line).append("\n");
            }
        }

        return convertedText.toString();
    }
}
