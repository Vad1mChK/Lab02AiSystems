defense_attorney('Phoenix Wright').
defense_attorney('Mia Fey').
prosecutor('Winston Payne').
thief('Frank Sahwit').
character('The Judge').
character('Larry Butz').
character('Cindy Stone').

friend('Phoenix Wright', 'Larry Butz').
friend('Phoenix Wright', 'Mia Fey').
friend('Larry Butz', 'Cindy Stone').
assists('Phoenix Wright', 'Mia Fey').
kills('Frank Sahwit', 'Cindy Stone').

character(X) :-
    defense_attorney(X),
    prosecutor(X),
    criminal(X),
    dead(X).
friends(X, Y) :- friend(X, Y); friend(Y, X).
killer(X) :- kills(X, _).
victim(X, Y) :- kills(Y, X).
criminal(X) :- killer(X); thief(X).
dead(X) :- character(X), victim(X, _).