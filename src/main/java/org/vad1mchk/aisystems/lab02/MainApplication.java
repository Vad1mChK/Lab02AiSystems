package org.vad1mchk.aisystems.lab02;

import org.vad1mchk.aisystems.lab02.knowledge.PrologQueryEngine;
import org.vad1mchk.aisystems.lab02.knowledge.RecommendationService;

public class MainApplication {
    public static void main(String[] args) {
        PrologQueryEngine engine = new PrologQueryEngine("aai_characters.pl");
        RecommendationService recommendationService = new RecommendationService(engine);
        UserInteractionHandler userHandler = new UserInteractionHandler(recommendationService);
        userHandler.startDialogue();
    }
}
