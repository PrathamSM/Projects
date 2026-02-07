package com.assessmentprod.questionBankService.service;

import com.assessmentprod.questionBankService.dto.CreateQuesReq;
import com.assessmentprod.questionBankService.dto.OpIdAndText;
import com.assessmentprod.questionBankService.dto.QuestionDto;
import com.assessmentprod.questionBankService.dto.QuestionWithCorrectAns;
import com.assessmentprod.questionBankService.entity.QuestionOption;
import com.assessmentprod.questionBankService.entity.Question;
import com.assessmentprod.questionBankService.exception.ResourceNotFoundException;
import com.assessmentprod.questionBankService.exception.SubjectNotFoundException;
import com.assessmentprod.questionBankService.repository.QuestionsRepository;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class QuestionService {

    @Autowired
    private QuestionsRepository quesRepo;

    //POST
    public Question createQuestion(CreateQuesReq createQuesReq) {
        Question question = new Question();

        question.setQuesText(createQuesReq.quesText());
        question.setSubject(createQuesReq.subject());


        List<QuestionOption> options = createQuesReq.options().stream().map(
                opText -> {
                    QuestionOption option = new QuestionOption();
                    option.setOptionText(opText);
                    option.setIsCorrect(opText.equals(createQuesReq.correctOption()));
                    option.setQuestion(question);
                    return option;
                }).collect(Collectors.toList());

        question.setOptions(options);
        return quesRepo.save(question);

    }

    //GET QUestions By Subject
    public List<QuestionDto> getQuestionsBySubject(String subject) {
        Optional<List<Question>> questions = quesRepo.findBySubject(subject);



        if(questions.isEmpty() || questions.get().isEmpty()) {
            throw new SubjectNotFoundException(subject);
        }

        return mapQuesToQuesDTO(questions.get());
    }


    public List<String> getListOfOptions(List<QuestionOption> options) {
        return options.stream().map(
                QuestionOption::getOptionText
        ).collect(Collectors.toList());
    }


        @SneakyThrows
        public QuestionDto getQuestionById(Long id) {
        Optional<Question> fetchedQuestion= quesRepo.findById(id);
        if(fetchedQuestion.isEmpty()) {
            throw new ResourceNotFoundException("Question", id);
        }

        Question question = fetchedQuestion.get();
        List<OpIdAndText> options = question.getOptions().stream().map(
                option -> {
                    return new OpIdAndText(option.getId(), option.getOptionText());
                }
        ).collect(Collectors.toList());
        return new QuestionDto(question.getId(), question.getQuesText(), options);


    }

    public List<OpIdAndText> getListOfOptionsWithIdAndText(List<QuestionOption> options) {
       return options.stream()
               .map( option -> {
                            return new OpIdAndText(option.getId(), option.getOptionText());
                       }
               ).collect(Collectors.toList());
    }

    public List<QuestionDto> mapQuesToQuesDTO(List<Question> questions) {


        //new req
        return questions.stream().map(
                question -> {
                    List<OpIdAndText> optionIdAndText = getListOfOptionsWithIdAndText(question.getOptions());

                    QuestionDto questionDto = new QuestionDto(question.getId(), question.getQuesText(), optionIdAndText);
                    return questionDto;
                }
        ).collect(Collectors.toList());
    }



    public List<QuestionWithCorrectAns> getQuesWithCorrectAns(List<Long> ids) {
        List<Question> questions = quesRepo.findByIdIn(ids);
        return mapQuesToQuesAnsDTO(questions);

    }



    public List<QuestionWithCorrectAns> mapQuesToQuesAnsDTO(List<Question> questions) {
        return questions.stream().map(
                question -> {
                    Long ansId = question.getOptions()
                            .stream().filter(QuestionOption::getIsCorrect)
                            .map(QuestionOption::getId)
                            .findFirst().orElse(null);

                    QuestionWithCorrectAns quesWithCorrAns = new QuestionWithCorrectAns(question.getId(), ansId);
                    return quesWithCorrAns;
                }
        ).collect(Collectors.toList());
    }




    //new req
    public List<QuestionDto> getQuestionsByQuestionIds(List<Long> ids) {
        List<Question> questions = quesRepo.findByIdIn(ids);

        List<QuestionDto> quesDTOs = mapQuesToQuesDTO(questions);
        return quesDTOs;
    }



    //PUT
    @SneakyThrows
    public Question updateQuestion(Long id, Question updatedQuestion) {
    Question question = quesRepo.findById(id)
            .orElseThrow(() -> new ResourceNotFoundException("Question", id));

        question.setQuesText(updatedQuestion.getQuesText());
        question.setSubject(updatedQuestion.getSubject());

    // Clear existing options and add updated options
        question.getOptions().clear();
        question.getOptions().addAll(updatedQuestion.getOptions());
        updatedQuestion.getOptions().forEach(option -> option.setQuestion(question));

        return quesRepo.save(question);
}

    //DELETE
    @SneakyThrows
    public boolean deleteQuestion(Long id) {
        Optional<Question> questionOptional = quesRepo.findById(id);
        if (questionOptional.isPresent()) {
            quesRepo.delete(questionOptional.get());
            return true;
        }
        else {
            throw new ResourceNotFoundException("Question", id);
        }
    }
}
