package co.analisys.gimnasio.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.core.FanoutExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.amqp.support.converter.MessageConverter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Bean;

@Configuration
@ConditionalOnProperty(name = "notifications.enabled", havingValue = "true", matchIfMissing = true)
public class RabbitMQConfig {

    private static final Logger log = LoggerFactory.getLogger(RabbitMQConfig.class);

    @Value("${app.notifications.exchange:miembros.notifications}")
    private String exchangeName;

    @Value("${app.notifications.queue:miembros.signup.notifications}")
    private String queueName;

    @Value("${app.notifications.routing-key:miembros.signup}")
    private String routingKey;

    // --- Configuración para cambios de horario de clases ---
    public static final String CLASES_HORARIO_EXCHANGE = "clase.horario.cambios";
    public static final String CLASES_HORARIO_MIEMBROS_QUEUE = "clase.horario.cambios.miembros";
    public static final String CLASES_HORARIO_ENTRENADORES_QUEUE = "clase.horario.cambios.entrenadores";

    @Bean
    public TopicExchange notificationExchange() {
        log.info("Configuring RabbitMQ Exchange: {}", exchangeName);
        return new TopicExchange(exchangeName, true, false);
    }

   

    /**
     * Exchange tipo fanout para eventos de cambio de horario de clase.
     */
    @Bean
    public FanoutExchange claseHorarioExchange() {
        log.info("Configuring FanoutExchange for cambios de horario: {}", CLASES_HORARIO_EXCHANGE);
        return new FanoutExchange(CLASES_HORARIO_EXCHANGE, true, false);
    }

    /**
     * Cola para notificaciones de miembros sobre cambios de horario.
     */
    @Bean
    public Queue claseHorarioMiembrosQueue() {
        log.info("Configuring Queue para miembros: {}", CLASES_HORARIO_MIEMBROS_QUEUE);
        return new Queue(CLASES_HORARIO_MIEMBROS_QUEUE, true);
    }

    /**
     * Cola para notificaciones de entrenadores sobre cambios de horario.
     */
    @Bean
    public Queue claseHorarioEntrenadoresQueue() {
        log.info("Configuring Queue para entrenadores: {}", CLASES_HORARIO_ENTRENADORES_QUEUE);
        return new Queue(CLASES_HORARIO_ENTRENADORES_QUEUE, true);
    }

    /**
     * Binding de la cola de miembros al exchange de cambios de horario.
     */
    @Bean
    public Binding claseHorarioMiembrosBinding(Queue claseHorarioMiembrosQueue, FanoutExchange claseHorarioExchange) {
        log.info("Binding miembros queue to clase horario exchange");
        return BindingBuilder.bind(claseHorarioMiembrosQueue).to(claseHorarioExchange);
    }

    /**
     * Binding de la cola de entrenadores al exchange de cambios de horario.
     */
    @Bean
    public Binding claseHorarioEntrenadoresBinding(Queue claseHorarioEntrenadoresQueue, FanoutExchange claseHorarioExchange) {
        log.info("Binding entrenadores queue to clase horario exchange");
        return BindingBuilder.bind(claseHorarioEntrenadoresQueue).to(claseHorarioExchange);
    }

    @Bean
    public MessageConverter jacksonMessageConverter(org.springframework.http.converter.json.Jackson2ObjectMapperBuilder builder) {
        return new Jackson2JsonMessageConverter(builder.build());
    }

    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory, MessageConverter jacksonMessageConverter) {
        RabbitTemplate template = new RabbitTemplate(connectionFactory);
        template.setMessageConverter(jacksonMessageConverter);
        return template;
    }
}
