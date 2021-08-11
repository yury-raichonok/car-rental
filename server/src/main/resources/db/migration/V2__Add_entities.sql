INSERT INTO role (id, role, role_description)
VALUES (1, 'USER', 'User'),
       (2, 'ADMIN', 'Administrator' );

INSERT INTO user (email, password, is_email_confirmed, locked, enabled, created_at, role_id)
VALUES ('carrental.webapp@gmail.com', '$2a$10$vhEpkW1a1QcN3vQinNi1XuvWuLj7dCytpIe4kemj/ZQ51rlxJqWM2',
        true, false, true, '2021-08-10', 2);

INSERT INTO location (id, name, coordinate_x, coordinate_y, zoom, created_at)
VALUES (1, 'Minsk, Zhilunovicha st., 47', 53.868605, 27.627085, 15, '2021-08-10');

INSERT INTO location_translation (id, name, language, location_id)
VALUES (1, 'Минск, ул. Жилуновича, 47', 'ru', 1),
       (2, 'Miнск, вул. Жылуновiча, 47', 'be', 1);

INSERT INTO rental_details (email, phone, payment_bill_validity_period_in_minutes,
                            from_day_to_week_coefficient, from_week_coefficient, location_id)
VALUES ('carrental.webapp@gmail.com', '+375111234567', 60, 0.92, 0.85, 1);

INSERT INTO car_class (id, name, created_at)
VALUES (1, 'Econom', '2021-08-10'),
       (2, 'Premium', '2021-08-10');

INSERT INTO car_class_translation (id, name, language, car_class_id)
VALUES (1, 'Эконом', 'ru', 1),
       (2, 'Эканом', 'be', 1),
       (3, 'Примиум', 'ru', 2),
       (4, 'Прэмiум', 'be', 2);

INSERT INTO faq (id, question, answer, created_at)
VALUES (1, 'What documents do I need to book a car?',
        'To book a car you need:
        Passport and driver\'s license;
        Credit or debit card to pay for the order;
        To receive a car, you need a printout of the paid order, which can be obtained in your personal account.',
        '2021-08-10'),
       (2, 'How old do I have to be to get a rental car?',
        'You must have a driving experience of 1 year and older and 19 years of age (for citizens of Belarus).
        Driving experience from 2 years, age from 20 years (for foreign citizens).', '2021-08-10'),
       (3, 'All taxes and fees are included in the rental price?',
        'All our rental offers include theft insurance, local taxes, airport taxes and tolls.',
        '2021-08-10'),
       (4, 'What should I pay attention to when choosing a car?',
        'Space: You will enjoy your rental more if you choose a vehicle that is spacious enough for all passengers and luggage.
        Location: The most convenient way to get a car is at the rental point nearest to you.',
        '2021-08-10');

INSERT INTO faq_translation (id, question, answer, language, faq_id)
VALUES (1, 'Какие документы мне нужны, чтобы забронировать автомобиль?',
        'Для бронирования автомобиля необходимы:
        Паспорт и водительское удостоверение;
        Кредитная или дебетовая карта для оплаты заказа;
        Для получения автомобиля необходима распечатка оплаченного заказа, которую можно получить в личном кабинете.',
        'ru', 1),
       (2, 'Якія дакументы мне патрэбныя, каб забраніраваць аўтамабіль?',
        'Для браніравання аўтамабіля неабходныя:
        Пашпарт і вадзіцельскае пасведчанне;
        Крэдытная або дэбетавая карта для аплаты замовы;
        Для атрымання аўтамабіля неабходная раздрукоўка аплачанай замовы, якую можна атрымаць у асабістым кабінеце.',
        'be', 1),
       (3, 'Сколько мне должно быть лет, чтобы арендовать машину?',
        'У Вас должен быть водительский стаж от 1 года и возраст от 19 лет (для граждан Беларуси).
        Водительский стаж от 2 лет, возраст от 20 лет (для иностранных граждан)', 'ru', 2),
       (4, 'Колькі мне павінна быць гадоў, каб арандаваць машыну?',
        'У Вас павінен быць вадзіцельскі стаж ад 1 года і ўзрост ад 19 гадоў (для грамадзян Беларусі).
        Вадзіцельскі стаж ад 2 гадоў, узрост ад 20 гадоў (для замежных грамадзян)', 'be', 2),
       (5, 'Все налоги и сборы включены в стоимость аренды?',
        'Все наши предложения по аренде включают страховку от кражи, местные налоги, аэропортовые сборы и дорожные сборы.',
        'ru', 3),
       (6, 'Усе падаткі і зборы ўключаны ў кошт арэнды?',
        'Усе нашы прапановы па арэндзе ўключаюць страхоўку ад крадзяжу, мясцовыя падаткі, аэрапортавыя зборы і дарожныя зборы.',
        'be', 3),
       (7, 'На что следует обращать внимание при выборе автомобиля?',
        'Пространство: вы получите больше удовольствия от аренды, если выберете автомобиль, достаточно просторный для всех пассажиров и багажа.
        Расположение: Самый удобный способ получить машину в ближайшем для Вас пункте проката.',
        'ru', 4),
       (8, 'На што варта звяртаць увагу пры выбары аўтамабіля?',
        'Прастору: Вы атрымаеце больш задавальнення ад арэнды, калі вылучыце аўтамабіль, досыць прасторны для ўсіх пасажыраў і багажу.
        Размяшчэнне: Самы зручны спосаб атрымаць машыну ў найбліжэйшай для Вас пункце пракату.',
        'be', 4);

INSERT INTO brand (id, brand_image_link, name, created_at)
VALUES (1, 'https://carrental-brand-images.s3.eu-west-2.amazonaws.com/1/1-unnamed.png', 'Lada',
        '2021-08-10'),
       (2, 'https://carrental-brand-images.s3.eu-west-2.amazonaws.com/2/2-renault-logo.png',
        'Renault', '2021-08-10'),
       (3, 'https://carrental-brand-images.s3.eu-west-2.amazonaws.com/3/3-43.970x0%402x.png',
        'Ford', '2021-08-10');

INSERT INTO model (id, name, created_at, brand_id)
VALUES (1, 'Granta', '2021-08-10', 1),
       (2, 'Duster', '2021-08-10', 2),
       (3, 'Mondeo', '2021-08-10', 3),
       (4, 'Explorer', '2021-08-10', 3);

INSERT INTO car (id, vin, date_of_issue, color, body_type, is_automatic_transmission, engine_type,
                 passengers_amt, baggage_amt, has_conditioner, cost_per_hour, in_rental,
                 car_image_link, created_at, model_id, car_class_id, rental_location_id)
VALUES (1, 'qwersdf', '2010-08-10', 'Orange', 1, 0, 1, 5, 3, 0, 5, 1,
        'https://carrental-cars-images.s3.eu-west-2.amazonaws.com/1/1-4_glb.jpg', '2021-08-10', 1,
        1, 1);