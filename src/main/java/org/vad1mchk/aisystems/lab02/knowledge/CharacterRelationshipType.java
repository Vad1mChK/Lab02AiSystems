package org.vad1mchk.aisystems.lab02.knowledge;

public enum CharacterRelationshipType {
    KILLS,
    ASSISTS,
    FRIENDS,
    VICTIM;

    private final String relationshipName;

    CharacterRelationshipType() {
        relationshipName = name().toLowerCase();
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public CharacterRelationshipType byName(String relationshipName) {
        for (CharacterRelationshipType relationshipType : CharacterRelationshipType.values()) {
            if (relationshipType.getRelationshipName().equals(relationshipName)) {
                return relationshipType;
            }
        }
        return null;
    }
}
