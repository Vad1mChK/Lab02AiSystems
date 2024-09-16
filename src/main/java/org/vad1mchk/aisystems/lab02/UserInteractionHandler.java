package org.vad1mchk.aisystems.lab02;

import org.vad1mchk.aisystems.lab02.knowledge.CharacterRelationshipType;
import org.vad1mchk.aisystems.lab02.knowledge.CharacterRole;
import org.vad1mchk.aisystems.lab02.knowledge.RecommendationService;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static org.vad1mchk.aisystems.lab02.util.StringUtils.startsWithIgnoreCase;

public class UserInteractionHandler {
    private static final Pattern TELL_ABOUT_PATTERN = Pattern.compile(
            "(?i)Tell me about\\s+([A-Z][A-Za-z\\-\\s]+)\\."
    );
    private static final Pattern DISLIKE_BY_NAME_PATTERN = Pattern.compile(
            "(?i)I(\\s+also)?\\s+don't like ([A-Z][A-Za-z\\-\\s]+)\\."
    );
    private static final Pattern LIKE_DISLIKE_BY_ROLE_PATTERN = Pattern.compile(
            "(?i)I(\\s+also)?(\\s+don't)?\\s+like people who are\\s+([A-Za-z][A-Za-z\\-\\s]+)\\."
    );
    private static final Pattern LIKE_DISLIKE_BY_RELATIONSHIP_PATTERN = Pattern.compile(
            "(?i)I(\\s+also)?(\\s+don't)?\\s+like people that\\s+([a-z\\-\\s]+)\\s+" +
                    "the person\\s+([A-Z][A-Za-z\\-\\s]+)\\."
    );
    private final RecommendationService recommendationService;
    private final Scanner scanner;
    public UserInteractionHandler(RecommendationService service) {
        this.recommendationService = service;
        this.scanner = new Scanner(System.in);
    }

    // Main method to handle user interaction
    public void startDialogue() {
        System.out.println("Welcome to the Ace Attorney Investigations Recommendation System!");
        System.out.println("It will recommend you characters who satisfy the conditions you specify.");
        System.out.println("Enter \"Help me\" for more information, or another query to get started right away.");

        run();
    }

    private void run() {
        while (true) {
            System.out.print("> ");
            String input = scanner.nextLine();

            CommandResult result = handleInput(input);
            if (result.equals(CommandResult.EXIT)) {
                break;
            }
        }
    }

    private CommandResult handleInput(String input) {
        if (startsWithIgnoreCase(input, "help me")) {
            return handleHelpCommand();
        }
        if (startsWithIgnoreCase(input, "exit") ||
                startsWithIgnoreCase(input, "quit") ||
                startsWithIgnoreCase(input, "goodbye")) {
            return handleExitCommand();
        }
        if (TELL_ABOUT_PATTERN.matcher(input).matches()) {
            return handleTellAboutCommand(input);
        }
        if (LIKE_DISLIKE_BY_RELATIONSHIP_PATTERN.matcher(input).matches()) {
            return handleLikeDislikeByRelationshipCommand(input);
        }
        if (LIKE_DISLIKE_BY_ROLE_PATTERN.matcher(input).matches()) {
            return handleLikeDislikeByRoleCommand(input);
        }
        if (DISLIKE_BY_NAME_PATTERN.matcher(input).matches()) {
            return handleDislikeByNameCommand(input);
        }
        System.out.println("[!] Unknown command. Type \"Help me\" to see valid commands.");
        return CommandResult.FAILURE;
    }

    private CommandResult handleExitCommand() {
        System.out.println("See you later!");
        return CommandResult.EXIT;
    }

    private CommandResult handleHelpCommand() {
        System.out.println("""
                Supported commands:
                - Help me : Show this help message.
                - Tell me about <Character>. : Displays general info about this character.
                - I [also] don't like <Character>. : Notes down that you don't like this character.
                - I [also] [don't] like people who are <Role>. : Notes down that you [don't] like
                  people of this <Role>.
                - I [also] [don't] like people that <Relationship> the person <Character>. :
                  Notes down that you [don't] like people that have this <Relationship> with this <Character>.
                - Exit / Quit / Goodbye : Exit this program.
                Supported <Role>s:
                - prosecutors, detectives, police officers, Interpol agents, Yatagarasu, criminals, thieves, killers,
                  dangerous criminals, smugglers, dead.
                Supported <Relationship>s:
                - kill, assist, are friends of, are victims of
                Notes:
                - Typing the optional "also" in your query will keep the context of the previous inquiries, up to the
                  last query without "also", or the first one in the conversation.
                  E. g.
                  ```
                  I like defense attorneys.
                  I also don't like criminals.
                  ```
                  will look for characters who satisfy all conditions, but
                  ```
                  I like defense attorneys.
                  I don't like criminals.
                  ```
                  will discard the previous conditions and look only for characters satisfying the last condition.
                - "who" is reserved for search by role, and "that" is reserved for search by relationship to other
                  characters.
                - You can only write one predicate per query. But you can combine predicates with "also".
                """);
        return CommandResult.SUCCESS;
    }

    private CommandResult handleTellAboutCommand(String input) {
        Matcher matcher = TELL_ABOUT_PATTERN.matcher(input);
        if (!matcher.matches()) {
            System.out.println("[!] Cannot parse command. Type \"Help me\" to see valid commands.");
            return CommandResult.FAILURE;
        }

        String character = matcher.group(1);

        if (!recommendationService.existsCharacter(character)) {
            System.out.println("[!] Character " + character + " does not exist.");
            return CommandResult.FAILURE;
        }

        System.out.println(character);
        System.out.println("Roles: " + String.join(
                ", ", recommendationService.rolesForCharacter(character)
        ));
        System.out.println("Friends: ");
        recommendationService.friendsForCharacter(character).forEach(it -> System.out.println("- " + it));
        return CommandResult.SUCCESS;
    }

    private CommandResult handleDislikeByNameCommand(String input) {
        Matcher matcher = DISLIKE_BY_NAME_PATTERN.matcher(input);
        if (!matcher.matches()) {
            System.out.println("[!] Cannot parse command. Type \"Help me\" to see valid commands.");
            return CommandResult.FAILURE;
        }
        boolean retainContext = matcher.group(1) != null && "also".equalsIgnoreCase(matcher.group(1).trim());
        String character = matcher.group(2);

        if (retainContext) {
            System.out.println("Got it. You don't like " + character + ", and would also like to preserve " +
                    "previous filters.");
        } else {
            System.out.println("Got it. You don't like " + character + ".");
        }

        List<String> resultList = recommendationService.recommendByDislikedPerson(character, retainContext);

        return processRecommendation(resultList);
    }

    private CommandResult handleLikeDislikeByRoleCommand(String input) {
        Matcher matcher = LIKE_DISLIKE_BY_ROLE_PATTERN.matcher(input);
        if (!matcher.matches()) {
            System.out.println("[!] Cannot parse command. Type \"Help me\" to see valid commands.");
            return CommandResult.FAILURE;
        }

        boolean retainContext = matcher.group(1) != null && "also".equalsIgnoreCase(matcher.group(1).trim());
        boolean dislike = matcher.group(2) != null && "don't".equalsIgnoreCase(matcher.group(2).trim());
        CharacterRole role = matcher.group(3) != null ?
                CharacterRole.byPluralName(matcher.group(3)) : null;

        if (role == null) {
            System.out.println("[!] Role '" + matcher.group(3) + "' not found. Allowed roles:");
            System.out.println("- " +
                    String.join(", ",
                            Arrays.stream(CharacterRole.values()).map(CharacterRole::getPluralName).toList())
            );
            return CommandResult.FAILURE;
        }

        if (retainContext) {
            System.out.println("Got it. You " +
                    (dislike ? "don't " : "") +
                    " like people with role: " +
                    role.getSingularName() +
                    ", and would also like to preserve previous filters.");
        } else {
            System.out.println("Got it. You " +
                    (dislike ? "don't " : "") +
                    "like people with role: " +
                    role.getSingularName() + ".");
        }


        List<String> resultList = dislike ?
                recommendationService.recommendByDislikedRole(role, retainContext) :
                recommendationService.recommendByLikedRole(role, retainContext);

        return processRecommendation(resultList);
    }

    private CommandResult handleLikeDislikeByRelationshipCommand(String input) {
        Matcher matcher = LIKE_DISLIKE_BY_RELATIONSHIP_PATTERN.matcher(input);
        if (!matcher.matches()) {
            System.out.println("[!] Cannot parse command. Type \"Help me\" to see valid commands.");
            return CommandResult.FAILURE;
        }
        boolean retainContext = matcher.group(1) != null && "also".equalsIgnoreCase(matcher.group(1).trim());
        boolean dislike = matcher.group(2) != null && "don't".equalsIgnoreCase(matcher.group(2).trim());
        CharacterRelationshipType relationshipType = matcher.group(3) != null ?
                CharacterRelationshipType.byPluralName(matcher.group(3).trim()) : null;
        String character = matcher.group(4) != null ? matcher.group(4).trim() : null;

        if (relationshipType == null) {
            System.out.println("[!] Relationship '" + matcher.group(3) + "' not found. Allowed relationships:");
            System.out.println("- " +
                    String.join(", " + Arrays.stream(CharacterRelationshipType.values())
                            .map(CharacterRelationshipType::getPluralName).toList()
                    ));
            return CommandResult.FAILURE;
        }
        if (character == null) {
            System.out.println("[!] Could not parse character name.");
            return CommandResult.FAILURE;
        }

        if (retainContext) {
            System.out.println("Got it. You " +
                    (dislike ? "don't " : "") +
                    "like people that " +
                    relationshipType.getPluralName() + " " + character +
                    ", and would also like to preserve previous filters.");
        } else {
            System.out.println("Got it. You " +
                    (dislike ? "don't " : "") +
                    "like people that " +
                    relationshipType.getPluralName() + " " + character);
        }

        List<String> resultList = dislike ?
                recommendationService.recommendByDislikedRelationship(relationshipType, character, retainContext) :
                recommendationService.recommendByLikedRelationship(relationshipType, character, retainContext);

        return processRecommendation(resultList);
    }

    private CommandResult processRecommendation(List<String> resultList) {
        if (resultList.isEmpty()) {
            System.out.println("[!] No recommendations found.");
            return CommandResult.FAILURE;
        }

        System.out.println("Here are the characters that might be to your liking (" + resultList.size() + ").");
        resultList.forEach(it -> System.out.println("- " + it));
        return CommandResult.SUCCESS;
    }

    private enum CommandResult {
        SUCCESS,
        FAILURE,
        EXIT
    }
}
