package com.assessmentprod.evaluationserver.evaluationserver.listener;

import com.assessmentprod.evaluationserver.evaluationserver.dto.SubmissionDTO;
import com.assessmentprod.evaluationserver.evaluationserver.service.EvaluationService;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;



@Component
public class EvaluationListener {

    @Autowired
    private EvaluationService evaluationService;

    @RabbitListener(queues = "submission_queue")
    public void receiveMessage(SubmissionDTO submissionDTO) {
        
        System.out.println("Received message: " + submissionDTO);
       
        evaluationService.evaluateSubmissionFromQueue(submissionDTO);
    }
}
