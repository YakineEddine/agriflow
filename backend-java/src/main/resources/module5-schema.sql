-- ============================================
-- MODULE 5 : COLLABORATIONS
-- Script SQL pour intégration au projet commun
-- ============================================

-- Table collab_requests (demandes de collaboration)
CREATE TABLE IF NOT EXISTS collab_requests (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    requester_id BIGINT NOT NULL,
    title VARCHAR(150) NOT NULL,
    description TEXT,
    start_date DATE,
    end_date DATE,
    needed_people INT DEFAULT 1,
    status ENUM('PENDING', 'APPROVED', 'REJECTED', 'CLOSED') DEFAULT 'PENDING',
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (requester_id) REFERENCES users(id) ON DELETE CASCADE,
    INDEX idx_status (status),
    INDEX idx_dates (start_date, end_date)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Demandes de collaboration entre agriculteurs';

-- Table collab_applications (candidatures)
CREATE TABLE IF NOT EXISTS collab_applications (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    request_id BIGINT NOT NULL,
    candidate_id BIGINT NOT NULL,
    message VARCHAR(255),
    status ENUM('PENDING', 'APPROVED', 'REJECTED') DEFAULT 'PENDING',
    applied_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (request_id) REFERENCES collab_requests(id) ON DELETE CASCADE,
    FOREIGN KEY (candidate_id) REFERENCES users(id) ON DELETE CASCADE,
    UNIQUE KEY unique_application (request_id, candidate_id),
    INDEX idx_request (request_id),
    INDEX idx_candidate (candidate_id)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci
COMMENT='Candidatures aux demandes de collaboration';

-- Données de test (optionnel - à adapter selon les users du Module 1)
INSERT INTO collab_requests (requester_id, title, description, start_date, end_date, needed_people, status) VALUES
(2, 'Récolte olives - Nabeul', 'Besoin de 2 personnes pour récolte olives bio', '2026-02-10', '2026-02-15', 2, 'PENDING'),
(3, 'Plantation tomates - Bizerte', 'Recherche 3 agriculteurs expérimentés', '2026-03-01', '2026-03-05', 3, 'APPROVED'),
(4, 'Irrigation orangers - Sousse', 'Besoin expertise irrigation goutte-à-goutte', '2026-02-20', '2026-02-22', 1, 'PENDING');

INSERT INTO collab_applications (request_id, candidate_id, message, status) VALUES
(1, 3, 'Expert en récolte, disponible ces dates', 'APPROVED'),
(1, 4, 'Je suis disponible et j''ai de l''expérience', 'PENDING'),
(2, 2, 'Intéressé par cette mission', 'PENDING');
