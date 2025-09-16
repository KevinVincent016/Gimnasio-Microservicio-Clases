@echo off
echo Iniciando Kafka para Gimnasio...
docker-compose up -d

echo Esperando que Kafka esté listo...
timeout /t 30

echo Creando topics necesarios...
docker exec gimnasio-kafka kafka-topics --create --topic ocupacion-clases --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
docker exec gimnasio-kafka kafka-topics --create --topic datos-entrenamiento --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1
docker exec gimnasio-kafka kafka-topics --create --topic resumen-entrenamiento --bootstrap-server localhost:9092 --partitions 3 --replication-factor 1

echo Kafka configurado y listo para usar!
pause