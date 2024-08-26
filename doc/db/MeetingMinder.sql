DROP DATABASE IF EXISTS MeetingMinder;
CREATE DATABASE MeetingMinder;
USE MeetingMinder;

-- Table des rôles
CREATE TABLE roles (
	id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(20) NOT NULL UNIQUE
);

-- Table des utilisateurs
CREATE TABLE users (
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(50) NOT NULL UNIQUE,
	password VARCHAR(255) NOT NULL,
	role_id INT NOT NULL,
    FOREIGN KEY (role_id) REFERENCES roles(id) ON DELETE RESTRICT ON UPDATE CASCADE
);

-- Table des salles de réunions
CREATE TABLE rooms (
	id INT AUTO_INCREMENT PRIMARY KEY,
	name VARCHAR(100) NOT NULL UNIQUE,
	capacity INT NULL,
	description TEXT NULL
);

-- Table des reservations
CREATE TABLE reservations (
	id INT AUTO_INCREMENT PRIMARY KEY,
	start_time DATETIME NOT NULL,
	end_time DATETIME NOT NULL,
	description TEXT NULL,
    created_at DATETIME DEFAULT CURRENT_TIMESTAMP,
    updated_at DATETIME DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
	user_id INT NOT NULL,
	room_id INT NOT NULL,
	FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (room_id) REFERENCES rooms(id) ON DELETE CASCADE ON UPDATE CASCADE
);

-- Insertion de rôles par défaut
INSERT INTO roles (name) 
VALUES 
('admin'),
('user');

-- Insertion de utilisateurs par défaut
INSERT INTO users (name, password, role_id)
VALUES
('admin', 'admin', 1),
('user_1', 'user_1', 2),
('user_2', 'user_2', 2),
('user_3', 'user_3', 2),
('user_4', 'user_4', 2);

-- Insertion de salles de réunion par défaut
INSERT INTO rooms (name, capacity, description)
VALUES 
('Salle Alpha', 10, 'Projecteur, Tableau blanc'),
('Salle Beta', 20, 'Téléconférence, Tableau blanc'),
('Salle Gamma', 15, 'Projecteur, Conférence téléphonique'),
('Salle Delta', 25, 'Écran plat, Tableau blanc, Vidéoconférence');

INSERT INTO reservations (start_time, end_time, description, user_id, room_id) VALUES 
('2024-08-07 09:00:00', '2024-08-07 10:00:00', 'Réunion de planification de projet', 2, 1),
('2024-08-07 11:00:00', '2024-08-07 12:30:00', 'Session de formation', 3, 2),
('2024-08-08 14:00:00', '2024-08-08 15:00:00', 'Réunion avec les clients', 4, 3),
('2024-08-08 09:00:00', '2024-08-08 11:00:00', 'Réunion de brainstorming', 5, 4),
('2024-08-09 10:00:00', '2024-08-09 11:00:00', 'Réunion de suivi de projet', 2, 1),
('2024-08-09 13:00:00', '2024-08-09 14:30:00', 'Présentation des résultats', 3, 2);