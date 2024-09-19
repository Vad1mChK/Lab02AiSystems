/* 
    This knowledge base contains facts about characters from the game Ace Attorney Investigations (2009).
    SPOILER ALERT! May contain major spoilers to the game.
*/

:- discontiguous thief/1.
:- discontiguous smuggler/1.
:- discontiguous criminal/1.
:- discontiguous character/1.
:- discontiguous assists/2.

% Character definitions
% Prosecutors
prosecutor('Jacques Portsman').
prosecutor('Miles Edgeworth').
prosecutor('Franziska von Karma').
prosecutor('Byrne Faraday').
prosecutor('Manfred von Karma').
% Detectives
detective('Buddy Faith').
detective('Dick Gumshoe').
detective('Ema Skye').
detective('Tyrell Badd').
% police force
police_officer('Buddy Faith').
police_officer('Dick Gumshoe').
police_officer('Maggey Byrde').
police_officer('Mike Meekins').
police_officer('Tyrell Badd').
% Interpol agents
interpol_agent('Akbey Hicks').
interpol_agent('Shi-Long Lang').
% Defense attorneys
defense_attorney('Calisto Yew').
% The Great Thief Yatagarasu
yatagarasu('Byrne Faraday').
yatagarasu('Tyrell Badd').
yatagarasu('Calisto Yew').
yatagarasu('Kay Faraday').
% Smugglers
smuggler('Quercus Alba').
% Thieves
thief('Ka-Shi Nou').
% Misc. criminals
criminal('Colin Devorae').
% Other misc. characters
character('Zinc Lablanc').
character('Rhoda Teneiro').
character('Cammy Meele').
character('Lance Amano').
character('Ernest Amano').
character('Wendy Oldbag').
character('Mack Rell').
character('Cece Yew').
character('Deid Mann').
character('The Judge').
character('Larry Butz').
character('Colias Palaeno').

% Friendships
friend('Miles Edgeworth', 'Dick Gumshoe').
friend('Miles Edgeworth', 'Franziska von Karma').
friend('Miles Edgeworth', 'Kay Faraday').
friend('Miles Edgeworth', 'Larry Butz').
friend('Dick Gumshoe', 'Maggey Byrde').
friend('Dick Gumshoe', 'Kay Faraday').
friend('Akbey Hicks', 'Shi-Long Lang').
friend('Byrne Faraday', 'Tyrell Badd').
% Murders
kills('Jacques Portsman', 'Buddy Faith').
kills('Cammy Meele', 'Akbey Hicks').
kills('Lance Amano', 'Colin Devorae').
kills('Manny Coachen', 'Cece Yew').
kills('Mack Rell', 'Deid Mann').
kills('Calisto Yew', 'Mack Rell').
kills('Calisto Yew', 'Byrne Faraday').
kills('Quercus Alba', 'Ka-Shi Nou').
kills('Quercus Alba', 'Manny Coachen').
% Assistances
assists('Kay Faraday', 'Miles Edgeworth').
assists('Dick Gumshoe', 'Miles Edgeworth').
assists('Jacques Portsman', 'Quercus Alba').
assists('Cammy Meele', 'Quercus Alba').
assists('Ernest Amano', 'Quercus Alba').
assists('Calisto Yew', 'Quercus Alba').
assists('Manny Coachen', 'Quercus Alba').

% Rules
friends(X, Y) :- friend(X, Y); friend(Y, X).  % Symmetrical friendship check.
victim(X, Y) :- kills(Y, X).  % A murder victim is the one who's killed.
dead(X) :- character(X), victim(X, _).  % A dead person can be a murder victim (For now, we don't have characters who die from other causes).
thief(X) :- yatagarasu(X).  % The Yatagarasu is a noble, vigilante thief, but not technically a criminal.
killer(X) :- kills(X, _).  % A killer is a person who kills someone.
smuggler(X) :- assists(X, 'Quercus Alba'). % Quercus Alba is the leader of the smuggling ring. His direct assistants are smugglers.
dangerous_criminal(X) :- smuggler(X).  % Smugglers are one type of dangerous criminals that the Interpol pursues.
criminal(X) :-
    dangerous_criminal(X);
    killer(X);
    thief(X), \+ yatagarasu(X). % Other types of criminals, pursued by the regular police, include thieves and killers.
character(X) :-
    prosecutor(X);
    detective(X);
    police_officer(X);
    interpol_agent(X);
    defense_attorney(X);
    yatagarasu(X);
    criminal(X);
    dead(X).  % First and foremost, people of all roles are characters.


% Queries of varying difficulty:

% Simple queries:
/*
    ?- prosecutor('Miles Edgeworth').
        % Is Miles Edgeworth a prosecutor?
        % true
    ?- yatagarasu('Kay Faraday').
        % Is Kay Faraday a Yatagarasu?
        % true
    ?- detective('Dick Gumshoe').
        % Is Dick Gumshoe a detective?
        % true
    ?- interpol_agent('Miles Edgeworth').
        % Is Miles Edgeworth an Interpol agent>
        % false
    ?- police_officer('Rhoda Teneiro').
        % Is Rhoda Teneiro a police officer?
        % false
*/

% Queries using logical operators (AND, OR, NOT)
/*
    ?- detective('Dick Gumshoe'), police_officer('Dick Gumshoe').
        % Is Dick Gumshoe a police detective?
        % true
    ?- prosecutor('Miles Edgeworth'); defense_attorney('Miles Edgeworth').  
        % Is Miles Edgeworth either a prosecutor or a defense attorney?
        % true
    ?- yatagarasu('Byrne Faraday'), yatagarasu('Tyrell Badd'), yatagarasu('Calisto Yew').  
        % Are Byrne Faraday, Tyrell Badd, and Calisto Yew all Yatagarasu?
        % true
    ?- prosecutor('Manfred von Karma'), \+ yatagarasu('Manfred von Karma').  
        % Is Manfred von Karma a prosecutor and not a Yatagarasu? 
        % true
    ?- \+ character('Godot').  
        % Is Godot is not a character, at least in this knowledge base?
        % true
*/

% Queries using variables to search for objects, probably using rules
/*
    ?- yatagarasu(Member). 
        % Members of Yatagarasu:
        % Member = 'Byrne Faraday' ;
        % Member = 'Tyrell Badd' ;
        % Member = 'Calisto Yew' ;
        % Member = 'Kay Faraday'.
    ?- smuggler(Smuggler), \+killer(Smuggler).
        % Smugglers who are not killers:
        % Smuggler = 'Ernest Amano' ;
    ?- setof(Character, character(Character), Characters).
        % Set of non-duplicate characters (registered in the knowledge base).
        % Because  just `character(X).` would return duplicates, since there are several routes for some characters.
        % Characters = ['Akbey Hicks', 'Buddy Faith', 'Byrne Faraday', 'Calisto Yew', 'Cammy Meele', 'Cece Yew', 'Colias Palaeno', 'Colin Devorae', 'Deid Mann'|...].
    ?- setof(Dead, (dead(Dead), \+ criminal(Dead)), DeadList).
        % Set of dead people who are not criminals:
        % DeadList = ['Akbey Hicks', 'Buddy Faith', 'Byrne Faraday', 'Cece Yew', 'Deid Mann'].
    ?- setof(Thief, (thief(Thief), \+criminal(Thief)), Thieves).
        % Set of thieves who are not criminals (obviously, Yatagarasu members who have not committed other crimes).
        % Thieves = ['Byrne Faraday', 'Kay Faraday', 'Tyrell Badd'].
*/