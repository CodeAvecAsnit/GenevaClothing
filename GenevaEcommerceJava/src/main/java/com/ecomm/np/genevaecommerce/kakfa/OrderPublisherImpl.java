package com.ecomm.np.genevaecommerce.kakfa;

import com.ecomm.np.genevaecommerce.extra.exception.OrderPublishException;
import com.ecomm.np.genevaecommerce.model.dto.WarehouseData;
import com.ecomm.np.genevaecommerce.model.entity.OrderedItems;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.support.SendResult;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.concurrent.CompletableFuture;

@Service
public class OrderPublisherImpl implements OrderPublisher {

    private Logger log = LoggerFactory.getLogger(OrderPublisher.class);
    private final String kafkaTopic;
    private final ObjectMapper objectMapper;
    private final KafkaTemplate<String, String> kafkaTemplate;

    public OrderPublisherImpl(
            @Value("${order.publish.topic}") String kafkaTopic,
            ObjectMapper objectMapper,
            KafkaTemplate<String, String> kafkaTemplate) {
        Assert.hasText(kafkaTopic, "Kafka topic must not be blank");
        this.kafkaTopic  = kafkaTopic;
        this.objectMapper = objectMapper;
        this.kafkaTemplate = kafkaTemplate;
    }

    @Override
    public void publishOrder(String email, OrderedItems orderedItems) {
        Assert.hasText(email, "Email must not be blank");
        Assert.notNull(orderedItems, "OrderedItems must not be null");
        String payload;
        try {
            WarehouseData warehouseData = WarehouseData.buildFromOrder(orderedItems);
            payload = objectMapper.writeValueAsString(warehouseData);
        } catch (JsonProcessingException e) {
            log.error("[Kafka] Failed to serialize OrderedItems for email={}, orderId={}",
                    email, orderedItems.getoId(), e);
            throw new OrderPublishException("Failed to serialize order payload",e);
        }

        log.debug("[Kafka] Publishing order to topic={}, email={}, orderId={}",
                kafkaTopic, email, orderedItems.getoId());

        CompletableFuture<SendResult<String, String>> future =
                kafkaTemplate.send(kafkaTopic, email, payload);

        future.whenComplete((result, ex) -> {
            if (ex != null) {
                log.error("[Kafka] Failed to publish order to topic={}, email={}, orderId={}",
                        kafkaTopic, email, orderedItems.getoId(), ex);
            } else {
                log.info("[Kafka] Order published successfully to topic={}, partition={}, offset={}, email={}, orderId={}",
                        kafkaTopic,
                        result.getRecordMetadata().partition(),
                        result.getRecordMetadata().offset(),
                        email,
                        orderedItems.getoId());
            }
        });
    }
}