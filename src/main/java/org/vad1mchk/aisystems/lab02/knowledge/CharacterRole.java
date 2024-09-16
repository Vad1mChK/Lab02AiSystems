package org.vad1mchk.aisystems.lab02.knowledge;

public enum CharacterRole {
    PROSECUTOR("prosecutor", "prosecutors"),
    DETECTIVE("detective", "detectives"),
    POLICE_OFFICER("police officer", "police officers"),
    INTERPOL_AGENT("Interpol agent", "Interpol agents"),
    DEFENSE_ATTORNEY("defense attorney", "defense attorneys"),
    YATAGARASU("Yatagarasu", "Yatagarasu"),
    CRIMINAL("criminal", "criminals"),
    THIEF("thief", "thieves"),
    KILLER("killer", "killers"),
    DANGEROUS_CRIMINAL("dangerous criminal", "dangerous criminals"),
    SMUGGLER("smuggler", "smugglers"),
    DEAD("dead", "dead");

    private final String roleName;
    private final String singularName;
    private final String pluralName;


    CharacterRole(String singularName, String pluralName) {
        roleName = name().toLowerCase();
        this.singularName = singularName;
        this.pluralName = pluralName;
    }

    public String getRoleName() {
        return roleName;
    }

    public String getSingularName() { return singularName; }

    public String getPluralName() { return pluralName; }

    public static CharacterRole byRoleName(String roleName) {
        for (CharacterRole role : CharacterRole.values()) {
            if (role.getRoleName().equals(roleName)) {
                return role;
            }
        }
        return null;
    }

    public static CharacterRole byPluralName(String pluralName) {
        for (CharacterRole role : CharacterRole.values()) {
            if (role.getPluralName().equals(pluralName)) {
                return role;
            }
        }
        return null;
    }
}
