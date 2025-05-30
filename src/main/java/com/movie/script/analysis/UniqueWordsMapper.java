package com.movie.script.analysis;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.HashSet;
import java.util.StringTokenizer;

public class UniqueWordsMapper extends Mapper<Object, Text, Text, Text> {
    private Text character = new Text();
    private Text word = new Text();
    
    public enum Counter {
        TOTAL_UNIQUE_WORDS_IDENTIFIED,
        NUMBER_OF_CHARACTERS_SPEAKING
    }

    private HashSet<String> uniqueWords = new HashSet<>();

    @Override
    public void map(Object key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] parts = line.split(":", 2);
        if (parts.length < 2) return;

        character.set(parts[0].trim());
        context.getCounter(Counter.NUMBER_OF_CHARACTERS_SPEAKING).increment(1);

        StringTokenizer tokenizer = new StringTokenizer(parts[1].trim());
        while (tokenizer.hasMoreTokens()) {
            String cleanedWord = tokenizer.nextToken().toLowerCase().replaceAll("[^a-zA-Z]", "");
            if (!cleanedWord.isEmpty() && uniqueWords.add(cleanedWord)) {
                word.set(cleanedWord);
                context.write(character, word);
                context.getCounter(Counter.TOTAL_UNIQUE_WORDS_IDENTIFIED).increment(1);
            }
        }
    }
}
