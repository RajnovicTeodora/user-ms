INSERT INTO role_table (id, name)
VALUES (1, 'HOST');
INSERT INTO role_table (id, name)
VALUES (2, 'GUEST');

-- LOZINKE SVIH KORISNIKA SU 'test' :)

insert into address (country, city, street, streetNum)
values ('Serbia', 'Novi Sad', 'Bulevar Oslobodjenja', 123);

insert into address (country, city, street, streetNum)
values ('Serbia', 'Novi Sad', 'Vojvodjanska', 321);

insert into address (country, city, street, streetNum)
values ('Serbia', 'Novi Sad', 'Jevrejska', 987);

insert into address (country, city, street, streetNum)
values ('Serbia', 'Novi Sad', 'narodnog fronta', 456);

insert into system_user (username, password, email, isDeleted, name, surname, notificationsActive, address_id, role_id)
values ('host123', '$2y$10$t4NZP3qGGdzGakospEzFHOPQngmjvi7dZeZSiwfiNz.1rv/smO0Ce', 'host@gmail.com', 'False', 'Miloje', 'Milovanovic', 'False', 1, 1);
insert into host (id)
values (1);

insert into system_user (username, password, email, isDeleted, name, surname, notificationsActive, address_id, role_id)
values ('guest123', '$2y$10$t4NZP3qGGdzGakospEzFHOPQngmjvi7dZeZSiwfiNz.1rv/smO0Ce', 'guest@gmail.com', 'False', 'Slavoje', 'Slovanovic', 'True', 2, 2);
insert into guest (id)
values (2);

insert into system_user (username, password, email, isDeleted, name, surname, notificationsActive, address_id, role_id)
values ('guest456', '$2y$10$t4NZP3qGGdzGakospEzFHOPQngmjvi7dZeZSiwfiNz.1rv/smO0Ce', 'guest2@gmail.com', 'False', 'Dragoje', 'Dragovic', 'True', 3, 2);
insert into guest (id)
values (3);

insert into system_user (username, password, email, isDeleted, name, surname, notificationsActive, address_id, role_id)
values ('host456', '$2y$10$t4NZP3qGGdzGakospEzFHOPQngmjvi7dZeZSiwfiNz.1rv/smO0Ce', 'host2@gmail.com', 'False', 'Blagoje', 'Blagovic', 'True', 4, 1);
insert into host (id)
values (4);

insert into rating (date, score, is_deleted, guest_id, is_host_rating, accommodation_id)
values ('2023-10-10', 4, false, 2, false, 1);

insert into rating (date, score, is_deleted, guest_id, is_host_rating, accommodation_id)
values ('2023-09-09', 3, false, 3, false, 1);

insert into rating (date, score, is_deleted, guest_id, is_host_rating, accommodation_id)
values ('2023-12-12', 5, false, 2, false, 2);

insert into rating (date, score, is_deleted, guest_id, is_host_rating, host_id, accommodation_id)
values ('2023-12-15', 5, false, 2, true, 1, -1);

insert into rating (date, score, is_deleted, guest_id, is_host_rating, host_id, accommodation_id)
values ('2023-09-10', 4, false, 3, true, 1, -1);

