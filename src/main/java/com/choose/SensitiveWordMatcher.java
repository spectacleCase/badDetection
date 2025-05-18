package com.choose;

import org.ahocorasick.trie.Emit;
import org.ahocorasick.trie.Trie;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SensitiveWordMatcher {
    private final Trie trie;
    private final Map<String, Sensitive> wordScores = new HashMap<>();

    public SensitiveWordMatcher(String filePath) throws IOException {
        List<String> lines = Files.readAllLines(Paths.get(filePath));
        //
        // Trie.TrieBuilder builder = Trie.builder().onlyWholeWords();

        Trie.TrieBuilder builder = Trie.builder();
        for (String line : lines) {
            if (line.trim().isEmpty()) {
                continue;
            }

            String[] parts = line.split("\\|");
            String word = TextPreprocessor.preprocess(parts[0].trim());

            builder.addKeyword(word);
            try {
                int score = parts.length > 1 ? Integer.parseInt(parts[2].trim()) : 10;
                wordScores.put(word, new Sensitive(parts[0].trim(), parts[1].trim(), score));
            } catch (Exception ignored) {

            }
        }

        this.trie = builder.build();
    }

    public Collection<Emit> match(String text) {
        return trie.parseText(text);
    }

    public int getScore(String word) {
        Sensitive sensitive = wordScores.get(word);
        if (sensitive == null) {
            return 0;
        }
        return sensitive.getScorer();
    }

    public String getType(String word) {
        Sensitive sensitive = wordScores.get(word);
        if (sensitive == null) {
            return null;
        }
        return sensitive.getType();

    }
}


