package com.assessmentprod.submissionservice.submissionservice.queues;

import com.assessmentprod.submissionservice.submissionservice.dto.SubmissionDTO;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;



@Service
public class RabbitMQProducer {

	@Autowired
    private final RabbitTemplate rabbitTemplate;

    
    public RabbitMQProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendMessage(SubmissionDTO submissionDTO) {
        rabbitTemplate.convertAndSend("submission_queue", submissionDTO);
    }
}
