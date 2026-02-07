package com.assessmentprod.testmgmt.service;

import com.assessmentprod.testmgmt.dto.*;
import com.assessmentprod.testmgmt.entity.TestEntity;
import com.assessmentprod.testmgmt.entity.TestQuestion;
import com.assessmentprod.testmgmt.exception.InvalidRequestException;
import com.assessmentprod.testmgmt.exception.ResourceNotFoundException;
import com.assessmentprod.testmgmt.repository.TestQuestionRepository;
import com.assessmentprod.testmgmt.repository.TestRepository;
import com.assessmentprod.testmgmt.service.client.QuestionBankClient;
import jakarta.transaction.Transactional;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class TestService {

    @Autowired
    private TestRepository testRepository;

    @Autowired
    private TestQuestionRepository testQuestionRepository;

    @Autowired
    private QuestionBankClient questionBankClient;

    public TestWithQuesIdsRes addTest(TestReq testReq) {
        if (testReq == null) {
            throw new InvalidRequestException("TestEntity request cannot be null.");
        }

        if (testReq.name() == null || testReq.name().isEmpty()) {
            throw new InvalidRequestException("TestEntity name cannot be null or empty.");
        }

        if (testReq.description() == null || testReq.description().isEmpty()) {
            throw new InvalidRequestException("TestEntity description cannot be null or empty.");
        }

        if (testReq.questionIds() == null || testReq.questionIds().isEmpty()) {
            throw new InvalidRequestException("At least one question ID must be provided.");
        }

        TestEntity test = new TestEntity();
        test.setName(testReq.name());
        test.setDescription(testReq.description());

        TestEntity savedTest = testRepository.save(test);

        List<TestQuestion> testQuestions = testReq.questionIds().stream()
                .map(quesId -> {
                    TestQuestion testQuestion = new TestQuestion();
                    testQuestion.setTest(savedTest);
                    testQuestion.setQuestionId(quesId);
                    return testQuestion;
                }).collect(Collectors.toList());

        testQuestionRepository.saveAll(testQuestions);
        savedTest.setTestQuestions(testQuestions);

        return new TestWithQuesIdsRes(savedTest.getId(), savedTest.getName(), savedTest.getDescription(), testReq.questionIds());
    }

    public TestResDto getTestById(Long testId) {
        TestEntity test = testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("TestEntity not found with ID: " + testId));

        List<Long> questionIds = getQuestionIdsByTestId(testId);
        List<QuestionDto> questions = questionBankClient.getQuestionsByQuesIds(questionIds);

        return new TestResDto(test.getId(), test.getName(), test.getDescription(), questions);
    }

    public List<Long> getQuestionIdsByTestId(Long testId) {
        if (!testRepository.existsById(testId)) {
            throw new ResourceNotFoundException("No test found with ID: " + testId);
        }

        List<TestQuestion> testQuestions = testQuestionRepository.findByTestId(testId);
        return testQuestions.stream().map(TestQuestion::getQuestionId).collect(Collectors.toList());
    }

    public List<TestWithDescRes> getAllTests() {
        List<TestEntity> tests = testRepository.findAll();
        return tests.stream()
                .map(test -> new TestWithDescRes(test.getId(), test.getName(), test.getDescription()))
                .collect(Collectors.toList());
    }

    @SneakyThrows
    @Transactional
    public TestWithQuesIdsRes updateTest(Long testId, UpdateTestReq updateTestReq) {
        if (updateTestReq == null) {
            throw new InvalidRequestException("Update request cannot be null.");
        }

        if (updateTestReq.name() == null || updateTestReq.name().isEmpty()) {
            throw new InvalidRequestException("TestEntity name cannot be null or empty.");
        }

        if (updateTestReq.description() == null || updateTestReq.description().isEmpty()) {
            throw new InvalidRequestException("TestEntity description cannot be null or empty.");
        }

        if (updateTestReq.questionIds() == null || updateTestReq.questionIds().isEmpty()) {
            throw new InvalidRequestException("At least one question ID must be provided.");
        }

        TestEntity test = testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("TestEntity not found with ID: " + testId));

        test.setName(updateTestReq.name());
        test.setDescription(updateTestReq.description());
        TestEntity updatedTest = testRepository.save(test);

        List<Long> updatedQuesIds = new ArrayList<>(getQuestionIdsByTestId(testId));
        updatedQuesIds.addAll(updateTestReq.questionIds());
        Set<Long> uniqueQuesIds = new HashSet<>(updatedQuesIds);

        testQuestionRepository.deleteByTestId(testId);

        List<TestQuestion> testQuestions = uniqueQuesIds.stream()
                .map(quesId -> {
                    TestQuestion testQuestion = new TestQuestion();
                    testQuestion.setTest(updatedTest);
                    testQuestion.setQuestionId(quesId);
                    return testQuestion;
                }).collect(Collectors.toList());

        testQuestionRepository.saveAll(testQuestions);

        return new TestWithQuesIdsRes(updatedTest.getId(), updatedTest.getName(), updatedTest.getDescription(), new ArrayList<>(uniqueQuesIds));
    }


    public void deleteTest(Long testId) {
        TestEntity test = testRepository.findById(testId)
                .orElseThrow(() -> new ResourceNotFoundException("TestEntity not found with ID: " + testId));

        testRepository.delete(test);
    }

//    public List<TestResDto> getTestsByIds(List<Long> testIds) {
//        List<TestEntity> tests = testRepository.findByIdIn(testIds);
//        if (tests.isEmpty()) {
//            throw new ResourceNotFoundException("Tests not found with IDs: " + testIds);
//        }
//        return tests.stream().map(test -> {
//            List<Long> questionIds = getQuestionIdsByTestId(test.getId());
//            List<QuestionDto> questions = questionBankClient.getQuestionsByQuesIds(questionIds);
//            return new TestResDto(test.getId(), test.getName(), test.getDescription(), questions);})
//                .collect(Collectors.toList());
//    }

    public List<TestResDto> getTestsByIds(List<Long> testIds) {
        if (testIds == null || testIds.isEmpty()) {
            throw new IllegalArgumentException("Test IDs must not be null or empty");
        }

        List<TestEntity> tests = testRepository.findByIdIn(testIds);

        if (tests.isEmpty()) {
            throw new ResourceNotFoundException("No tests found with IDs: " + testIds);
        }

        return tests.stream()
                .map(test -> {
                    List<Long> questionIds = getQuestionIdsByTestId(test.getId());
                    List<QuestionDto> questions = questionBankClient.getQuestionsByQuesIds(questionIds);
                    return new TestResDto(test.getId(), test.getName(), test.getDescription(), questions);
                })
                .collect(Collectors.toList());
    }


}
