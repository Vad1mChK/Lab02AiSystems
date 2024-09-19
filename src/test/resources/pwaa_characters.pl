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

friends(X, Y) :- friend(X, Y); friend(Y, X).
killer(X) :- kills(X, _).
victim(X, Y) :- kills(Y, X).
criminal(X) :- killer(X); thief(X).
dead(X) :- victim(X, _).