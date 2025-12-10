INSERT INTO words (word)
SELECT *
FROM CSVREAD('src/main/resources/db/dic.txt');