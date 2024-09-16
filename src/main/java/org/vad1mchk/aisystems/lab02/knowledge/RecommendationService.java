package org.vad1mchk.aisystems.lab02.knowledge;

import java.util.*;

public class RecommendationService {
    private final PrologQueryEngine prologEngine;
    private final RecommendationContext context;

    public RecommendationService(PrologQueryEngine prologEngine) {
        this.prologEngine = prologEngine;
        this.context = new RecommendationContext();
    }

    public boolean existsCharacter(String character) {
        return prologEngine.executeQueryCheckForSolution("character('" + character + "').");
    }

    public List<String> rolesForCharacter(String character) {
        return Arrays.stream(CharacterRole.values()).filter(
                role -> prologEngine.executeQueryCheckForSolution(
                        role.getRoleName() + "('" + character + "')."
                )
        ).map(CharacterRole::getSingularName).toList();
    }

    public List<String> friendsForCharacter(String character) {
        return prologEngine.executeQuerySortedAndFiltered("friends('" + character + "', Who).", "Who");
    }

    public List<String> recommendByLikedRole(CharacterRole role, boolean retainContext) {
        retainOrClearContext(retainContext);
        context.addLikedRole(role);
        return buildAndExecuteQuery();
    }

    public List<String> recommendByDislikedRole(CharacterRole role, boolean retainContext) {
        retainOrClearContext(retainContext);
        context.addDislikedRole(role);
        return buildAndExecuteQuery();
    }

    public List<String> recommendByLikedRelationship(
            CharacterRelationshipType relationshipType,
            String character,
            boolean retainContext
    ) {
        retainOrClearContext(retainContext);
        context.addLikedRelationship(relationshipType, character);
        return buildAndExecuteQuery();
    }

    public List<String> recommendByDislikedRelationship(
            CharacterRelationshipType relationshipType,
            String character,
            boolean retainContext
    ) {
        retainOrClearContext(retainContext);
        context.addDislikedRelationship(relationshipType, character);
        return buildAndExecuteQuery();
    }

    public List<String> recommendByDislikedPerson(String person, boolean retainContext) {
        retainOrClearContext(retainContext);
        context.addDislikedPerson(person);
        return buildAndExecuteQuery();
    }

    /**
     * Cleans the recommendation context before you add a new preference if you choose <b>not</b> to retain it.
     * Otherwise does nothing.
     *
     * @param retainContext Whether to retain context.
     */
    private void retainOrClearContext(boolean retainContext) {
        if (!retainContext) {
            context.clear();
        }
    }

    private List<String> buildAndExecuteQuery() {
        StringBuilder queryBuilder = new StringBuilder("character(Who)");
        for (CharacterRole role : context.getLikedRoles()) {
            queryBuilder
                    .append(", ")
                    .append(role.getRoleName())
                    .append("(Who)");
        }
        for (CharacterRole role : context.getDislikedRoles()) {
            queryBuilder
                    .append(", \\+ ")
                    .append(role.getRoleName())
                    .append("(Who)");
        }
        for (CharacterRelationship relationship : context.getLikedRelationships()) {
            queryBuilder
                    .append(", ")
                    .append(relationship.getRelationshipType().getRelationshipName())
                    .append("(Who, '")
                    .append(relationship.getCharacter())
                    .append("')");
        }
        for (CharacterRelationship relationship : context.getDislikedRelationships()) {
            queryBuilder
                    .append(", \\+")
                    .append(relationship.getRelationshipType().getRelationshipName())
                    .append("(Who, '")
                    .append(relationship.getCharacter())
                    .append("')");
        }
        for (String person : context.getDislikedPeople()) {
            queryBuilder
                    .append(", Who \\= '")
                    .append(person)
                    .append("'");
        }
        queryBuilder.append(".");
        System.out.println("Executing query: " + queryBuilder);
        return prologEngine.executeQuerySortedAndFiltered(queryBuilder.toString(), "Who");
    }

    /**
     * This class stores the user's preferences. It works according to the following rules:
     * - Initially, all characters can be recommended.
     * - If new preferences are added, the character will be retained if they satisfy all these rules:
     * - The character must satisfy all of the liked roles.
     * - The character must not be in any of the disliked roles.
     * - The character must satisfy all of the liked relationships.
     * - The character must not be in any of the disliked relationships.
     * - The character must not be any of the disliked people.
     */
    private static class RecommendationContext {
        private final EnumSet<CharacterRole> likedRoles;
        private final EnumSet<CharacterRole> dislikedRoles;
        private final Set<CharacterRelationship> likedRelationships;
        private final Set<CharacterRelationship> dislikedRelationships;
        private final Set<String> dislikedPeople;

        public RecommendationContext() {
            likedRoles = EnumSet.noneOf(CharacterRole.class);
            dislikedRoles = EnumSet.noneOf(CharacterRole.class);
            likedRelationships = new HashSet<>();
            dislikedRelationships = new HashSet<>();
            dislikedPeople = new HashSet<>();
        }

        public void addLikedRole(CharacterRole role) {
            likedRoles.add(role);
        }

        public void addDislikedRole(CharacterRole role) {
            dislikedRoles.add(role);
        }

        public void addLikedRelationship(CharacterRelationshipType relationshipType, String character) {
            likedRelationships.add(new CharacterRelationship(relationshipType, character));
        }

        public void addDislikedRelationship(CharacterRelationshipType relationshipType, String character) {
            dislikedRelationships.add(new CharacterRelationship(relationshipType, character));
        }

        public void addDislikedPerson(String character) {
            dislikedPeople.add(character);
        }

        public EnumSet<CharacterRole> getLikedRoles() {
            return likedRoles;
        }

        public EnumSet<CharacterRole> getDislikedRoles() {
            return dislikedRoles;
        }

        public Set<CharacterRelationship> getLikedRelationships() {
            return likedRelationships;
        }

        public Set<CharacterRelationship> getDislikedRelationships() {
            return dislikedRelationships;
        }

        public Set<String> getDislikedPeople() {
            return dislikedPeople;
        }

        public void clear() {
            likedRoles.clear();
            dislikedRoles.clear();
            likedRelationships.clear();
            dislikedRelationships.clear();
            dislikedPeople.clear();
        }
    }
}
