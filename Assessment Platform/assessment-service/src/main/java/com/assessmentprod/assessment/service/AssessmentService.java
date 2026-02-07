package com.assessmentprod.assessment.service;

import com.assessmentprod.assessment.dto.AssessmentReq;
import com.assessmentprod.assessment.dto.AssessmentResDto;
import com.assessmentprod.assessment.dto.AssessmentWithTestIdsRes;
import com.assessmentprod.assessment.dto.test.TestResDto;
import com.assessmentprod.assessment.entity.Assessment;
import com.assessmentprod.assessment.entity.AssessmentTest;
import com.assessmentprod.assessment.exception.BadRequestException;
import com.assessmentprod.assessment.repository.AssessmentRepository;
import com.assessmentprod.assessment.repository.AssessmentTestRepository;
import com.assessmentprod.assessment.service.client.TestClient;
import lombok.SneakyThrows;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class AssessmentService {

    @Autowired
    private AssessmentRepository assessmentRepository;

    @Autowired
    private AssessmentTestRepository assessmentTestRepository;

    @Autowired
    private TestClient testClient;



    public AssessmentWithTestIdsRes addAssessment(AssessmentReq assessmentReq) throws BadRequestException {
        if (assessmentReq.name() == null || assessmentReq.name().isEmpty()) {
            throw new BadRequestException("Name is required.");
        }
        if (assessmentReq.testIds() == null || assessmentReq.testIds().isEmpty()) {
            throw new BadRequestException("Test IDs are required.");
        }
        Assessment assessment = new Assessment();

        assessment.setName(assessmentReq.name());

        Assessment savedAssessment = assessmentRepository.save(assessment);

        List<AssessmentTest> assessmentTests = assessmentReq.testIds().stream()
                .map(testId -> {
                    AssessmentTest assessmentTest = new AssessmentTest();

                    assessmentTest.setAssessment(savedAssessment);
                    assessmentTest.setTestId(testId);
                    return assessmentTest;
                }).collect(Collectors.toList());

        assessmentTestRepository.saveAll(assessmentTests);

        savedAssessment.setAssessmentTests(assessmentTests);

        return new AssessmentWithTestIdsRes(savedAssessment.getId(), savedAssessment.getName(), assessmentReq.testIds());
    }


    public List<TestResDto> getTestsByIds(List<Long> testIds) {
        return testClient.getByMultipleTestIds(testIds);
    }

//    public TestResDto getTestById(Long testId) {
//        if(testRepository.findById(testId).isEmpty()) {
//            return null;
//        }
//        Test test = testRepository.findById(testId).get();
//
//        List<Long> questionIds = getQuestionIdsByTestId(testId);
//
//        List<QuestionDto> questions = questionBankClient.getQuestionsByQuesIds(questionIds);
//
//        return new TestResDto(test.getId(), test.getName(), test.getDescription(), questions);
//
//    }

    @SneakyThrows
    public AssessmentResDto getAssessmentById(Long assessmentId) {
        if(assessmentRepository.findById(assessmentId).isEmpty()) {
            throw new BadRequestException("Assessment doesn't exist with ID : " + assessmentId);
        }

        Assessment assessment = assessmentRepository.findById(assessmentId).get();

        List<Long> testIds = getTestIdsByAssessmentId(assessmentId);

        List<TestResDto> tests = testClient.getByMultipleTestIds(testIds);

        return new AssessmentResDto(assessmentId, assessment.getName(), tests);

    }


    public List<Long> getTestIdsByAssessmentId(Long assessmentId) {
        List<AssessmentTest> assessmentTests = assessmentTestRepository.findByAssessmentId(assessmentId);
        return assessmentTests.stream()
                .map(AssessmentTest::getTestId).collect(Collectors.toList());
    }

//    public List<Long> getQuestionIdsByTestId(Long testId) {
//        if(testRepository.findById(testId).isEmpty()) {
//            return null;
//        }
//
//        List<TestQuestion> testQuestions = testQuestionRepository.findByTestId(testId);
//
//        return testQuestions.stream()
//                .map(TestQuestion::getQuestionId).collect(Collectors.toList());
//    }



    //GET ALL ASSESSMENTS
    public List<AssessmentResDto> getAllAssessments() {
        return assessmentRepository.findAll().stream().map(assessment -> {
            List<Long> testIds = getTestIdsByAssessmentId(assessment.getId());
            List<TestResDto> tests = testClient.getByMultipleTestIds(testIds);
            return new AssessmentResDto(assessment.getId(), assessment.getName(), tests);
        }).collect(Collectors.toList());}

    //To be tested
    public void deleteAssessment(Long id) {
        assessmentRepository.deleteById(id);}




    @SneakyThrows
    public AssessmentWithTestIdsRes addTestsToAssessment(Long assessmentId, List<Long> testIds) {
        // Validate input parameters
        if (assessmentId == null) {
            throw new BadRequestException("Assessment ID cannot be null");
        }
        if (testIds == null || testIds.isEmpty()) {
            throw new BadRequestException("Test IDs cannot be null or empty");
        }

        // Fetch the existing Assessment by ID
        Assessment assessment = assessmentRepository.findById(assessmentId)
                .orElseThrow(() -> new BadRequestException("Assessment not found with ID: " + assessmentId));

        // Get existing test IDs associated with the assessment to prevent duplicates
        Set<Long> existingTestIds = assessment.getAssessmentTests()
                .stream()
                .map(AssessmentTest::getTestId)
                .collect(Collectors.toSet());

        // Filter out any test IDs that already exist in the assessment
        List<AssessmentTest> newAssessmentTests = testIds.stream()
                .filter(testId -> !existingTestIds.contains(testId))
                .map(testId -> {
                    AssessmentTest assessmentTest = new AssessmentTest();
                    assessmentTest.setAssessment(assessment);
                    assessmentTest.setTestId(testId);
                    return assessmentTest;
                })
                .collect(Collectors.toList());

        // Check if there are new tests to add
        if (newAssessmentTests.isEmpty()) {
            throw new BadRequestException("All provided test IDs already exist in the assessment");
        }

        // Save only the new AssessmentTests and associate them with the Assessment
        assessmentTestRepository.saveAll(newAssessmentTests);
        assessment.getAssessmentTests().addAll(newAssessmentTests);

        // Return updated assessment with test IDs
        return new AssessmentWithTestIdsRes(
                assessment.getId(),
                assessment.getName(),
                assessment.getAssessmentTests().stream()
                        .map(AssessmentTest::getTestId)
                        .collect(Collectors.toList())
        );
    }
}

