CREATE TABLE role (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    role VARCHAR (20) NOT NULL,
    role_description VARCHAR (255) NOT NULL
);

CREATE TABLE user (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR (255) NOT NULL UNIQUE,
    password VARCHAR (255) NOT NULL,
    is_email_confirmed TINYINT(1) NOT NULL,
    locked TINYINT(1) NOT NULL,
    enabled TINYINT(1) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    changed_at DATETIME(6),
    last_login DATETIME(6),
    role_id BIGINT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES role (id)
);

CREATE TABLE passport (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    first_name VARCHAR (255) NOT NULL,
    middle_name VARCHAR (255),
    last_name VARCHAR (255) NOT NULL,
    dob VARCHAR (10) NOT NULL,
    passport_series VARCHAR (10) NOT NULL,
    passport_number VARCHAR (10) UNIQUE NOT NULL,
    date_of_issue DATE NOT NULL,
    validity_period DATE NOT NULL,
    organization_that_issued VARCHAR (255) NOT NULL,
    status TINYINT(1) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    changed_at DATETIME(6),
    confirmed_at DATETIME(6),
    documents_file_link VARCHAR (255),
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE driving_license (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    date_of_issue DATE NOT NULL,
    validity_period DATE NOT NULL,
    organization_that_issued VARCHAR (255) NOT NULL,
    status TINYINT(1) NOT NULL,
    created_at DATETIME(6) NOT NULL,
    changed_at DATETIME(6),
    confirmed_at DATETIME(6),
    documents_file_link VARCHAR (255),
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE user_confirmation_token (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    creation_time DATETIME(6) NOT NULL,
    expiration_time DATETIME(6) NOT NULL,
    confirmation_time DATETIME(6),
    token VARCHAR (255) NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE phone (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    phone VARCHAR (25) NOT NULL,
    active TINYINT(1) NOT NULL,
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE brand (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    brand_image_link VARCHAR (255),
    name VARCHAR (30) UNIQUE NOT NULL,
    created_at DATETIME(6) NOT NULL,
    changed_at DATETIME(6)
);

CREATE TABLE car_class (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR (255) NOT NULL UNIQUE,
    created_at DATETIME(6) NOT NULL,
    changed_at DATETIME(6)
);

CREATE TABLE model (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR (30) UNIQUE NOT NULL,
    created_at DATETIME(6) NOT NULL,
    changed_at DATETIME(6),
    brand_id BIGINT NOT NULL,
    FOREIGN KEY (brand_id) REFERENCES brand (id)
);

CREATE TABLE location (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR (255) UNIQUE NOT NULL,
    coordinate_x DECIMAL (8,6) NOT NULL,
    coordinate_y DECIMAL (8,6) NOT NULL,
    zoom INT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    changed_at DATETIME(6)
);

CREATE TABLE rental_details (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    email VARCHAR (255) NOT NULL,
    phone VARCHAR (255) NOT NULL,
    payment_bill_validity_period_in_minutes INT NOT NULL,
    from_day_to_week_coefficient DECIMAL (3, 2) NOT NULL,
    from_week_coefficient DECIMAL (3, 2) NOT NULL,
    location_id BIGINT NOT NULL,
    FOREIGN KEY (location_id) REFERENCES location (id)
);

CREATE TABLE car (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    vin VARCHAR (20) UNIQUE NOT NULL,
    date_of_issue DATE NOT NULL,
    color VARCHAR (20) NOT NULL,
    body_type VARCHAR (20) NOT NULL,
    is_automatic_transmission TINYINT (1) NOT NULL,
    engine_type VARCHAR (15) NOT NULL,
    passengers_amt INT NOT NULL,
    baggage_amt INT NOT NULL,
    has_conditioner TINYINT(1) NOT NULL,
    cost_per_hour DECIMAL NOT NULL,
    in_rental TINYINT(1) NOT NULL,
    car_image_link VARCHAR (255),
    created_at DATETIME(6) NOT NULL,
    changed_at DATETIME(6),
    model_id BIGINT NOT NULL,
    car_class_id BIGINT NOT NULL,
    rental_location_id BIGINT NOT NULL,
    FOREIGN KEY (model_id) REFERENCES model (id),
    FOREIGN KEY (car_class_id) REFERENCES car_class (id),
    FOREIGN KEY (rental_location_id) REFERENCES location (id)
);

CREATE TABLE ord (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    pick_up_date DATETIME NOT NULL,
    return_date DATETIME NOT NULL,
    total_cost DECIMAL (10, 2) NOT NULL,
    payment_status TINYINT(1) NOT NULL,
    comments LONGTEXT,
    sent_date DATETIME NOT NULL,
    payment_date DATETIME,
    denying_date DATETIME,
    rental_status TINYINT(1) NOT NULL,
    car_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    location_id BIGINT NOT NULL,
    FOREIGN KEY (car_id) REFERENCES car (id),
    FOREIGN KEY (user_id) REFERENCES user (id),
    FOREIGN KEY (location_id) REFERENCES location (id)
);

CREATE TABLE faq (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question LONGTEXT NOT NULL,
    answer LONGTEXT NOT NULL,
    created_at DATETIME(6) NOT NULL,
    changed_at DATETIME(6)
);

CREATE TABLE message (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR (255) NOT NULL,
    email VARCHAR (255) NOT NULL,
    phone VARCHAR (20) NOT NULL,
    message LONGTEXT NOT NULL,
    sent_date DATETIME (6) NOT NULL,
    readed TINYINT(1) NOT NULL
);

CREATE TABLE payment_bill (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    total_cost DECIMAL(10,2) NOT NULL,
    sent_date DATETIME NOT NULL,
    expiration_time DATETIME NOT NULL,
    payment_date DATETIME,
    order_id BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES ord (id)
);

CREATE TABLE repair_bill (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    message LONGTEXT,
    total_cost DECIMAL(10,2) NOT NULL,
    sent_date DATETIME NOT NULL,
    payment_date DATETIME,
    order_id BIGINT NOT NULL,
    FOREIGN KEY (order_id) REFERENCES ord (id)
);

CREATE TABLE notification (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    message VARCHAR (255) NOT NULL,
    notification_type VARCHAR (20) NOT NULL,
    user_id BIGINT NOT NULL,
    sent_date DATETIME(6) NOT NULL,
    status TINYINT(1) NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE rental_request (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    request_type VARCHAR (255) NOT NULL,
    sent_date DATETIME(6) NOT NULL,
    consideration_date DATETIME(6),
    considered TINYINT(1) NOT NULL,
    comments VARCHAR (255),
    user_id BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES user (id)
);

CREATE TABLE faq_translation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    question LONGTEXT NOT NULL,
    answer LONGTEXT NOT NULL,
    language VARCHAR (2) NOT NULL,
    faq_id BIGINT NOT NULL,
    FOREIGN KEY (faq_id) REFERENCES faq (id)
);

CREATE TABLE location_translation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR (255) UNIQUE NOT NULL,
    language VARCHAR (2) NOT NULL,
    location_id BIGINT NOT NULL,
    FOREIGN KEY (location_id) REFERENCES location (id)
);

CREATE TABLE car_class_translation (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    name VARCHAR (255) NOT NULL UNIQUE,
    language VARCHAR (2) NOT NULL,
    car_class_id BIGINT NOT NULL,
    FOREIGN KEY (car_class_id) REFERENCES car_class (id)
);