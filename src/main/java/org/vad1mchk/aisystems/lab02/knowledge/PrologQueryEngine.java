package org.vad1mchk.aisystems.lab02.knowledge;

import org.jpl7.Query;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class PrologQueryEngine {
    public PrologQueryEngine(String knowledgeBaseFilePath) {
        loadKnowledgeBase(knowledgeBaseFilePath);
    }

    // Method to load the Prolog knowledge base file
    private void loadKnowledgeBase(String filePath) {
        String queryText = "consult('" + filePath + "')";
        Query query = new Query(queryText);
        if (query.hasSolution()) {
            System.out.println("Knowledge base loaded successfully.");
        } else {
            System.out.println("Failed to load knowledge base.");
        }
    }

    // Method to execute a Prolog query and return results as a string array
    public List<String> executeQuery(String queryText, String variable) {
        Query query = new Query(queryText);
        return queryResultsToList(query, variable);
    }

    public List<String> executeQuerySortedAndFiltered(String queryText, String variable) {
        return executeQuery(queryText, variable).stream()
                .distinct()
                .sorted()
                .toList();
    }

    public boolean executeQueryCheckForSolution(String queryText) {
        Query query = new Query(queryText);
        return query.hasSolution();
    }

    // Helper method to convert query results into a String array
    private List<String> queryResultsToList(Query query, String variable) {
        return Arrays.stream(query.allSolutions())
                .map(solution -> Objects.toString(solution.get(variable)))
                .toList();
    }
}
