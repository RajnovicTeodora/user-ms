INSERT INTO role_table (id, name)
VALUES (1, 'HOST');
INSERT INTO role_table (id, name)
VALUES (2, 'GUEST');

-- LOZINKE SVIH KORISNIKA SU 'test' :)

insert into address (country, city, street, streetNum, postCode)
values ('Serbia', 'Novi Sad', 'Bulevar Oslobodjenja', 123, 21220);

insert into address (country, city, street, streetNum, postCode)
values ('Serbia', 'Novi Sad', 'Vojvodjanska', 321, 21220);

insert into system_user (username, password, email, isDeleted, name, surname, notificationsActive, address_id, role_id)
values ('host123', '$2y$10$t4NZP3qGGdzGakospEzFHOPQngmjvi7dZeZSiwfiNz.1rv/smO0Ce', 'host@gmail.com', 'False', 'Miloje', 'Milovanovic', 'False', 1, 1);
insert into host (id)
values (1);

insert into system_user (username, password, email, isDeleted, name, surname, notificationsActive, address_id, role_id)
values ('guest123', '$2y$10$t4NZP3qGGdzGakospEzFHOPQngmjvi7dZeZSiwfiNz.1rv/smO0Ce', 'guest@gmail.com', 'False', 'Slavoje', 'Slovanovic', 'True', 2, 2);
insert into guest (id)
values (2);
