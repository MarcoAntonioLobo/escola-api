CREATE TABLE IF NOT EXISTS client (
    client_id INT AUTO_INCREMENT PRIMARY KEY,
    external_id VARCHAR(255),
    school_name VARCHAR(255) NOT NULL,
    cafeteria_name VARCHAR(255),
    location VARCHAR(255),
    student_count INT
);

CREATE TABLE IF NOT EXISTS client_data (
    data_id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    month_date DATE NOT NULL,
    cantinaPercent DECIMAL(5,2) DEFAULT 0,
    revenue DECIMAL(15,2) DEFAULT 0,
    expenses DECIMAL(15,2) DEFAULT 0,
    order_count INT DEFAULT 0,
    registered_students INT NOT NULL,
    notes TEXT,
    averageCantinaPerStudent DECIMAL(10,2) DEFAULT 0,
    averagePedagogicalPerStudent DECIMAL(10,2) DEFAULT 0,
    profitability DECIMAL(15,2) DEFAULT 0,
    revenueLoss DECIMAL(15,2) DEFAULT 0,
    ordersOutsideVpt INT DEFAULT 0,
    averageTicketApp DECIMAL(10,2) DEFAULT 0,
    UNIQUE (client_id, month_date),
    FOREIGN KEY (client_id) REFERENCES client(client_id) ON DELETE CASCADE
);