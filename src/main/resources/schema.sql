CREATE TABLE IF NOT EXISTS client (
    client_id INT PRIMARY KEY AUTO_INCREMENT,
    external_id VARCHAR(50),
    school_name VARCHAR(255),
    cafeteria_name VARCHAR(255),
    location VARCHAR(255),
    student_count INT
);

CREATE TABLE IF NOT EXISTS client_data (
    data_id INT PRIMARY KEY AUTO_INCREMENT,
    client_id INT,
    month_date DATE,
    revenue DECIMAL(10,2),
    expenses DECIMAL(10,2),
    order_count INT,
    notes VARCHAR(255),
    registered_students INT,
    FOREIGN KEY (client_id) REFERENCES client(client_id)
);
