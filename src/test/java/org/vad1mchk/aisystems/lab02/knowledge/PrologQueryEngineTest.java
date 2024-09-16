package org.vad1mchk.aisystems.lab02.knowledge;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class PrologQueryEngineTest {
    private PrologQueryEngine prologEngine;
    private Path tempKnowledgeBase;

    @BeforeEach
    public void setUp() throws IOException {
        // Load the test KB file from src/test/resources/
        InputStream kbStream = getClass().getClassLoader().getResourceAsStream("pwaa_characters.pl");
        assertNotNull(kbStream, "Knowledge base file not found in resources.");

        // Copy the knowledge base to a temporary file
        tempKnowledgeBase = Files.createTempFile("test_kb", ".pl");
        Files.copy(kbStream, tempKnowledgeBase, StandardCopyOption.REPLACE_EXISTING);

        // Initialize PrologQueryEngine with the temporary KB file
        prologEngine = new PrologQueryEngine(
                tempKnowledgeBase.toAbsolutePath().toString().replace("\\", "/")
        );
    }

    @Test
    public void testQueryExecution_SimpleQuery() {
        // Test a simple query for defense attorneys
        List<String> defenseAttorneys = prologEngine.executeQuery("defense_attorney(X)", "X");
        assertEquals(2, defenseAttorneys.size());
        assertTrue(defenseAttorneys.contains("Phoenix Wright"));
        assertTrue(defenseAttorneys.contains("Mia Fey"));
    }

    @Test
    public void testQueryExecution_CheckForSolution() {
        // Check if Phoenix Wright is a defense attorney
        boolean isPhoenixDefenseAttorney = prologEngine.executeQueryCheckForSolution("defense_attorney('Phoenix Wright')");
        assertTrue(isPhoenixDefenseAttorney);

        // Check if Larry Butz is a defense attorney (should be false)
        boolean isLarryDefenseAttorney = prologEngine.executeQueryCheckForSolution("defense_attorney('Larry Butz')");
        assertFalse(isLarryDefenseAttorney);
    }

    @Test
    public void testSortedAndFilteredQueryExecution() {
        // Test sorted and filtered query for friends of Phoenix Wright
        List<String> friendsOfPhoenix = prologEngine.executeQuerySortedAndFiltered("friends('Phoenix Wright', Y)", "Y");
        assertEquals(2, friendsOfPhoenix.size());
        assertEquals(List.of("Larry Butz", "Mia Fey"), friendsOfPhoenix);  // Sorted alphabetically
    }

    @Test
    public void testCriminalsQuery() {
        // Test query for criminals (thieves or killers)
        List<String> criminals = prologEngine.executeQuery("criminal(X)", "X");

        assertEquals(2, criminals.size());
        assertTrue(criminals.contains("Frank Sahwit"));  // Thief
        assertTrue(criminals.contains("Frank Sahwit"));  // Also killer of Cindy Stone
    }

    @Test
    public void testDeadPeopleQuery() {
        // Test query for dead people (victims)
        List<String> deadPeople = prologEngine.executeQuery("dead(X)", "X");

        assertEquals(1, deadPeople.size());
        assertTrue(deadPeople.contains("Cindy Stone"));
    }
}
