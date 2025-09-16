package co.analisys.gimnasio.config;

import co.analisys.gimnasio.dto.kafka.DatosEntrenamiento;
import co.analisys.gimnasio.dto.kafka.ResumenEntrenamiento;
import org.apache.kafka.common.serialization.Serdes;
import org.apache.kafka.streams.StreamsBuilder;
import org.apache.kafka.streams.kstream.*;
import org.apache.kafka.streams.KeyValue;
import org.apache.kafka.streams.state.Stores;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.kafka.support.serializer.JsonSerde;

import java.time.Duration;

@Configuration
public class KafkaStreamsConfig {
    
    @Bean
    public KStream<String, DatosEntrenamiento> kStream(StreamsBuilder streamsBuilder) {
        
        JsonSerde<DatosEntrenamiento> datosSerde = new JsonSerde<>(DatosEntrenamiento.class);
        JsonSerde<ResumenEntrenamiento> resumenSerde = new JsonSerde<>(ResumenEntrenamiento.class);
        
        KStream<String, DatosEntrenamiento> stream = streamsBuilder
            .stream("datos-entrenamiento", Consumed.with(Serdes.String(), datosSerde));

        // Procesar datos de entrenamiento en ventanas de 7 días
        KTable<Windowed<String>, ResumenEntrenamiento> resumenTable = stream
            .groupByKey(Grouped.with(Serdes.String(), datosSerde))
            .windowedBy(TimeWindows.ofSizeWithNoGrace(Duration.ofDays(7)))
            .aggregate(
                () -> new ResumenEntrenamiento(),
                (key, value, aggregate) -> aggregate.actualizar(value),
                Materialized.<String, ResumenEntrenamiento>as(
                    Stores.persistentTimestampedWindowStore("resumen-entrenamiento-store",
                                                           Duration.ofDays(30),
                                                           Duration.ofDays(7), false))
                    .withKeySerde(Serdes.String())
                    .withValueSerde(resumenSerde)
            );

        resumenTable
            .toStream()
            .map((windowedKey, value) -> KeyValue.pair(windowedKey.key(), value))
            .to("resumen-entrenamiento", Produced.with(Serdes.String(), resumenSerde));

        return stream;
    }
}