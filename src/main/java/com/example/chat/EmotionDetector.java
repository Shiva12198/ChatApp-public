package com.example.chat;

import java.util.*;
import java.util.regex.*;

public class EmotionDetector {
    private final Map<String, Set<String>> emotionWords = new HashMap<>();
    private final Pattern wordPattern = Pattern.compile("\\b[\\p{L}'-]+\\b", Pattern.UNICODE_CHARACTER_CLASS);

    public EmotionDetector() {
        loadExpandedDictionary();
    }

    private void loadExpandedDictionary() {
        // 😊 HAPPY
        emotionWords.put("happy", new HashSet<>(Arrays.asList(
            "happy","happiness","joy","joyful","glad","pleased","delighted","ecstatic","cheerful","cheery",
            "smile","smiling","grateful","content","contented","satisfied","thrilled","excited","elated",
            "awesome","great","fantastic","wonderful","yay","wohoo","lol","haha","hehe","yayyy","amazing",
            "blessed","grinning","radiant","sunny","optimistic","uplifted","positive","good","best",
            ":)","(:",":-)",":D","=D","XD","xD","😊","😁","😄","😃","😆","😺"
        )));

        // 😢 SAD
        emotionWords.put("sad", new HashSet<>(Arrays.asList(
            "sad","sadness","unhappy","depressed","down","downcast","miserable","sorrow","sorrowful","blue",
            "cry","crying","tears","tearful","lonely","gloomy","melancholy","heartbroken","unloved",
            "grief","grieving","regret","hopeless","depressing","disappointed","pain","hurt",
            "tired","drained","bored","emo","broken",":((",":(","T_T","TT",":'(","😢","😭","😞","😔","☹","😟"
        )));

        // 😡 ANGRY
        emotionWords.put("angry", new HashSet<>(Arrays.asList(
            "angry","anger","mad","furious","irate","annoyed","annoying","rage","outraged","pissed",
            "frustrated","frustration","cross","fuming","infuriated","irritated","offended","agitated",
            "hate","hating","dislike","grr","grrr",">:(","x(","ragequit",">:-(","😠","😡","🤬","👿"
        )));

        // 😨 FEAR
        emotionWords.put("fear", new HashSet<>(Arrays.asList(
            "afraid","fear","scared","frightened","terrified","panic","anxious","anxiety","nervous","worried",
            "phobia","fearful","alarmed","shaking","shiver","tremble","tense","uneasy","insecure","paranoid",
            "aghast","creeped","startled","😨","😰","😱","😖","😬","😧","😟"
        )));

        // 😲 SURPRISE
        emotionWords.put("surprised", new HashSet<>(Arrays.asList(
            "surprised","surprise","shocked","astonished","amazed","startled","stunned","whoa","omg",
            "wow","wtf","what","unexpected","unbelievable","cantbelieve","jawdrop","holy","😲","😮","😯","🤯"
        )));

        // 🤢 DISGUST
        emotionWords.put("disgust", new HashSet<>(Arrays.asList(
            "disgust","disgusted","gross","nasty","repulsed","sickened","revolted","ew","eww","yuck","ugh",
            "nauseous","vomit","vomiting","barf","repulsive","dirty","🤢","🤮","😖"
        )));

        // 😍 LOVE
        emotionWords.put("love", new HashSet<>(Arrays.asList(
            "love","loving","adore","adored","fond","cherish","cherished","affection","affectionate",
            "sweet","sweetheart","darling","dear","cute","cutie","beautiful","handsome","heart","hearts",
            "romantic","boyfriend","girlfriend","bff","xoxo","kiss","hug","<3","❤","💖","💕","💞","💓",
            "💗","😍","😘","😚","😙","🥰"
        )));

        // 😐 NEUTRAL
        emotionWords.put("neutral", new HashSet<>(Arrays.asList(
            "ok","okay","fine","alright","meh","whatever","hmm","huh","idk","hmmm","hmm..","neutral","normal"
        )));
    }

    // Detect the strongest emotion present
    public DetectedEmotion detect(String username, String text) {
        if (text == null || text.isEmpty()) return null;
        String lower = text.toLowerCase();

        Matcher matcher = wordPattern.matcher(lower);
        List<String> words = new ArrayList<>();
        while (matcher.find()) words.add(matcher.group());

        Map<String, Integer> counts = new HashMap<>();
        for (String key : emotionWords.keySet()) counts.put(key, 0);

        for (String w : words) {
            for (Map.Entry<String, Set<String>> e : emotionWords.entrySet()) {
                if (e.getValue().contains(w)) {
                    counts.put(e.getKey(), counts.get(e.getKey()) + 1);
                }
            }
        }

        for (Map.Entry<String, Set<String>> e : emotionWords.entrySet()) {
            for (String token : e.getValue()) {
                if (token.length() > 1 && lower.contains(token)) {
                    counts.put(e.getKey(), counts.get(e.getKey()) + 1);
                }
            }
        }

        String best = null;
        int bestCount = 0;
        for (Map.Entry<String, Integer> e : counts.entrySet()) {
            if (e.getValue() > bestCount) {
                best = e.getKey();
                bestCount = e.getValue();
            }
        }

        if (best == null || bestCount == 0) return null;
        return new DetectedEmotion(username, best, bestCount);
    }

    public static class DetectedEmotion {
        public final String username;
        public final String emotion;
        public final int score;

        public DetectedEmotion(String username, String emotion, int score) {
            this.username = username;
            this.emotion = emotion;
            this.score = score;
        }
    }
}
