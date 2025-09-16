CREATE TABLE IF NOT EXISTS kafka_offsets (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    topic_name VARCHAR(255) NOT NULL,
    partition_number INT NOT NULL,
    offset_value BIGINT NOT NULL,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    UNIQUE KEY unique_topic_partition (topic_name, partition_number)
);

-- Índices para mejorar rendimiento
CREATE INDEX idx_topic_name ON kafka_offsets(topic_name);
CREATE INDEX idx_updated_at ON kafka_offsets(updated_at);