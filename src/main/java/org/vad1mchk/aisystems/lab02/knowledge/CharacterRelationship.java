package org.vad1mchk.aisystems.lab02.knowledge;

import java.util.Objects;

public class CharacterRelationship {
    private final CharacterRelationshipType relationshipType;
    private final String character;

    public CharacterRelationship(CharacterRelationshipType relationshipType, String character) {
        this.relationshipType = relationshipType;
        this.character = character;
    }

    public CharacterRelationshipType getRelationshipType() {
        return relationshipType;
    }

    public String getCharacter() {
        return character;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof CharacterRelationship that)) return false;
        return relationshipType == that.relationshipType && Objects.equals(character, that.character);
    }

    @Override
    public int hashCode() {
        return Objects.hash(relationshipType, character);
    }

    @Override
    public String toString() {
        return "CharacterRelationship{" +
                "relationshipType=" + relationshipType +
                ", character='" + character + '\'' +
                '}';
    }
}
