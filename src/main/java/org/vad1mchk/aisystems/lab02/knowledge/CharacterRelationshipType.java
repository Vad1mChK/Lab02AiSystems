package org.vad1mchk.aisystems.lab02.knowledge;

public enum CharacterRelationshipType {
    KILLS("kill"),
    ASSISTS("assist"),
    FRIENDS("are friends of"),
    VICTIM("are victims of");

    private final String relationshipName;
    private final String pluralName;

    CharacterRelationshipType(String pluralName) {
        relationshipName = name().toLowerCase();
        this.pluralName = pluralName;
    }

    public static CharacterRelationshipType byRelationshipName(String relationshipName) {
        for (CharacterRelationshipType relationshipType : CharacterRelationshipType.values()) {
            if (relationshipType.getRelationshipName().equals(relationshipName)) {
                return relationshipType;
            }
        }
        return null;
    }

    public static CharacterRelationshipType byPluralName(String pluralName) {
        for (CharacterRelationshipType relationshipType : CharacterRelationshipType.values()) {
            if (relationshipType.getPluralName().equals(pluralName)) {
                return relationshipType;
            }
        }
        return null;
    }

    public String getRelationshipName() {
        return relationshipName;
    }

    public String getPluralName() {
        return pluralName;
    }
}
