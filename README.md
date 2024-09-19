# Лабораторная работа № 2 по дисциплине "Системы искусственного интеллекта"
3-й курс, Нейротехнологии и программирование, ПИиКТ, КТУ, ИТМО

Целью этой лабораторной работы является разработка программы (рекомендательной системы), которая будет использовать базу знаний или онтологию для предоставления рекомендаций на основе введенных пользователем данных. (Knowledge-based support system)

## Задание

- Создать программу, которая позволяет пользователю ввести запрос через командную строку. Например, информацию о себе, своих интересах и предпочтениях в контексте выбора видеоигры - на основе фактов из БЗ (из первой лабы)/~~Онтологии(из второй)~~.
- Использовать введенные пользователем данные, чтобы выполнить логические запросы к  БЗ/Онтологии.
- На основе полученных результатов выполнения запросов, система должна предоставить рекомендации или советы, связанные с выбором из БЗ или онтологии.
- Система должна выдавать рекомендации после небольшого диалога с пользователем.

## Описание предметной области

- **Осторожно, СПОЙЛЕРЫ**!
- База знаний основывается на серии визуальных новелл [*Ace Attorney*](https://ru.wikipedia.org/wiki/Ace_Attorney) и описывает персонажей этих видеоигр (большинство), их роли и отношения между ними. 
- [Основная база знаний](./aai_characters.pl) составлена на основе видеоигры [*Ace Attorney Investigations: Miles Edgeworth*](https://ru.wikipedia.org/wiki/Ace_Attorney_Investigations:_Miles_Edgeworth) (2009).
- [Тестовая база знаний](./src/test/resources/pwaa_characters.pl) составлена на основе [первого эпизода](https://aceattorney.fandom.com/wiki/The_First_Turnabout) видеоигры [*Phoenix Wright: Ace Attorney*](https://ru.wikipedia.org/wiki/Phoenix_Wright:_Ace_Attorney) (2001).

## Взаимодействие с системой рекомендации

- Взаимодействие происходит через интерфейс командной строки (CLI).
- Команды подаются на естественном языке, но в чётко структурированном формате.
- Доступные команды:
  - `Help me` -- справка по всем командам.
  - `Exit`, `Quit`, `Goodbye` -- выход из программы.
  - `Tell me about <CHARACTER>.` -- основная информация о персонаже `<CHARACTER>` (имя, роли, список друзей).
    - Пример: `Tell me about Miles Edgeworth.`
  - `I [also] don't like <CHARACTER>.` -- указать, что вам не нравится персонаж `<CHARACTER>` (добавить фильтр).
    - Пример: `I don't like Miles Edgeworth.`
  - `I [also] [don't] like people who are <ROLE>.` -- указать, что вам нравятся / не нравятся персонажи определённой роли `<ROLE>` (добавить фильтр).
     - Пример: `I don't like people who are prosecutors.`
  - `I [also] [don't] like people that <RELATIONSHIP> the person <CHARACTER>` -- указать, что вам нравятся / не нравятся персонажи, имеющие отношение `<RELATIONSHIP>` к персонажу `<ROLE>`.
     - Пример: `I also like people that are friends of the person Miles Edgeworth.`
- Доступные роли (`<ROLE>`) во множественном числе:
  - `prosecutors`
  - `detectives`
  - `police officers`
  - `Interpol agents`
  - `Yatagarasu`
  - `criminals`
  - `thieves`
  - `killers`
  - `dangerous criminals`
  - `smugglers`
  - `dead`
- Доступные отношения (`<RELATIONSHIP>`) во множественном числе:
  - `kill`
  - `assist`
  - `are friends of`
  - `are victims of`
- Все фильтры соединяются конъюнкцией. Система ищет персонажей, каждый из которых соответствует **всем** фильтрам.
- В командах-фильтрах слово `also` означает, что вы хотели бы сохранить предыдущие фильтры (контекст поиска). Если его не указать, все предыдущие фильтры будут сброшены, и будет использован только последний.
  - Примеры:
    - Сохраняет контекст предыдущего запроса; выводит персонажей, которые адвокаты **и** не преступники:
      ```
      I like people who are defense attorneys.
      I also don't like people who are criminals.
      ```
    - Сбрасывает предыдущие фильтры; выводит всех персонажей, которые не преступники, при этом они могут и не быть адвокатами:
      ```
      I like people who are defense attorneys.
      I don't like people who are criminals.
      ```
- Перед запросом к базе знаний система выводит текст запроса:
  ```
  > I don't like Miles Edgeworth.
  Got it. You don't like Miles Edgeworth.
  Executing query: character(Who), Who \= 'Miles Edgeworth'.
  ```
- Затем система выводит список всех персонажей, которые соответствуют текущим фильтрам. Список отсортирован и содержит только уникальные имена.
  ```
  Here are the characters that might be to your liking (30).
  - Akbey Hicks
  - Buddy Faith
  - Byrne Faraday
  ...
  ```
- Если вы добавите противоречивые фильтры или система просто не найдёт персонажей по вашему запросу, будет выведено сообщение об ошибке.
  ```
  > I like people who are detectives.
  Got it. You like people with role: detective.
  Executing query: character(Who), detective(Who).
  Here are the characters that might be to your liking (4).
  ...
  > I also don't like people who are detectives.
  Got it. You don't like people with role: detective, and would also like to preserve previous filters.
  Executing query: character(Who), detective(Who), \+ detective(Who).
  [!] No recommendations found.
  ```
## Использованные технологии

- Java + JPL
- SWI-Prolog
