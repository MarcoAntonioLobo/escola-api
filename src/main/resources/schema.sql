-- ===============================
-- TABELA CLIENT
-- ===============================
CREATE TABLE IF NOT EXISTS client (
    client_id INT AUTO_INCREMENT PRIMARY KEY,
    external_id VARCHAR(50) NOT NULL,
    school_name VARCHAR(255) NOT NULL,
    cafeteria_name VARCHAR(255) NOT NULL,
    location VARCHAR(255),
    student_count INT
);

-- ===============================
-- TABELA CLIENT_DATA
-- ===============================
CREATE TABLE IF NOT EXISTS client_data (
    data_id INT AUTO_INCREMENT PRIMARY KEY,
    client_id INT NOT NULL,
    month_date DATE NOT NULL,
    cantina_percent DECIMAL(5,4),
    registered_students INT,
    average_cantina_per_student DECIMAL(10,2),
    average_pedagogical_per_student DECIMAL(10,2),
    order_count INT,
    revenue DECIMAL(15,2),
    profitability DECIMAL(15,2),
    revenue_loss DECIMAL(15,2),
    orders_outside_vpt INT,
    average_ticket_app DECIMAL(15,2),
    notes TEXT,
    FOREIGN KEY (client_id) REFERENCES client(client_id) ON DELETE CASCADE
);
