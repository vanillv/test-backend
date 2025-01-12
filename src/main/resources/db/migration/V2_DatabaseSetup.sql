create table budget
(
    id     serial primary key,
    year   int  not null,
    month  int  not null,
    amount int  not null,
    type   text not null
);
CREATE TABLE author (
    id SERIAL PRIMARY KEY,
    fio VARCHAR(255) NOT NULL,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP NOT NULL
);
ALTER TABLE budget ADD COLUMN author_id INT REFERENCES author(id);