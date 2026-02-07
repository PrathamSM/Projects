package com.example.file_upload1.file_upload1.service;
import com.example.file_upload1.file_upload1.dto.CreateProfileReq;
import com.example.file_upload1.file_upload1.dto.FeedbackRating;
import com.example.file_upload1.file_upload1.dto.ProfessionalExperienceRequestDTO;
import com.example.file_upload1.file_upload1.dto.ProfileStatus;
import com.example.file_upload1.file_upload1.exception.FileProcessingException;
import com.example.file_upload1.file_upload1.feign.FeedbackClient;
import com.example.file_upload1.file_upload1.feign.ProfessionalExperienceClient;
import com.example.file_upload1.file_upload1.feign.ProfileClient;
import com.example.file_upload1.file_upload1.feign.ProfileStatusClient;
import org.apache.tika.Tika;
import org.apache.tika.exception.TikaException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Service
public class FileProcessingService {

    @Autowired
    private ProfileClient profileClient;

    @Autowired
    private FeedbackClient feedbackClient;

    @Autowired
    private ProfessionalExperienceClient professionalExperienceClient;
@Autowired
private ProfileStatusClient profileStatusClient;
    private static final String EMAIL_REGEX = "[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}";
    private static final String PHONE_REGEX = "\\b\\d{10}\\b|(?:\\+\\d{1,3}[- ]?)?\\d{10}\\b";

    public List<CreateProfileReq> processAndStoreFiles(List<MultipartFile> files) throws FileProcessingException {
        List<CreateProfileReq> profiles = new ArrayList<>();
        for (MultipartFile file : files) {
            try {
                // Extract data from each file
                CreateProfileReq profile = extractData(file);

                // Send extracted profile data to profile-service
                ResponseEntity<?> response =    profileClient.createProfile(profile);
                Map<String, Object> responseBody = (Map<String, Object>) response.getBody();
                Long ProfileId =  Long.parseLong(responseBody.get("data").toString());

                createProfessionalExperience(ProfileId);
                profiles.add(profile);

                createFeedback(ProfileId);
                createProfileStatus(ProfileId);


            } catch (FileProcessingException e) {
                System.err.println("Failed to process file: " + file.getOriginalFilename() + " - " + e.getMessage());
            }
        }
        return profiles;
    }

    private void   createProfessionalExperience(Long ProfileId) {
        ProfessionalExperienceRequestDTO experienceRequest = new ProfessionalExperienceRequestDTO();
        ;
        experienceRequest.setProfileId(ProfileId);
        experienceRequest.setPrimaryDisciplines(new ArrayList<>());
        experienceRequest.setSecondaryDisciplines(new ArrayList<>());
        experienceRequest.setProfession(null);
        experienceRequest.setQualification(null);
        experienceRequest.setExperienceYears(null);
        experienceRequest.setRelevantExperience(null);
        professionalExperienceClient.addExperience(experienceRequest);
    }
    private void createFeedback(Long ProfileId ) {
        FeedbackRating feedbackRating = new FeedbackRating();
        feedbackRating.getFeedbackDate(LocalDateTime.now());
        feedbackRating.setProfileId(ProfileId);
        feedbackRating.setFeedback("good");
        feedbackRating.setGivenBy(23L);
        feedbackRating.setRating(3);

        feedbackClient.createFeedback(feedbackRating);

    }
    private void createProfileStatus (Long ProfileId){
        ProfileStatus profileStatus = new ProfileStatus();
        profileStatus.setProfileId(ProfileId);
        profileStatus.setNdaSigned(false);
        profileStatus.setAvailability(null); // Example availability
        profileStatus.setIsApproved(false);
        profileStatusClient.createProfileStatus(profileStatus);
    }


    private CreateProfileReq extractData(MultipartFile file) throws FileProcessingException {
        try {
            Tika tika = new Tika();
            String fileContent = tika.parseToString(file.getInputStream());

            // Extract name from filename
            String name = extractNameFromFileName(file.getOriginalFilename());

            // Extract email and phone number from file content using regex
            String email = extractUsingRegex(fileContent, EMAIL_REGEX, "Not Available");
            String phoneNumber = extractUsingRegex(fileContent, PHONE_REGEX, "Not Available");

            // Return profile data with extracted details
            return new CreateProfileReq(
                    name,
                    LocalDate.now(),
                    email,
                    "test.secondary@example.com",  // Replace with actual extracted secondary email if needed
                    "Test Address",
                    null,
                    null,
                    null,
                    "12345",
                    phoneNumber
            );
        } catch (IOException | TikaException e) {
            throw new FileProcessingException("Failed to process the file: " + e.getMessage());
        }
    }

    /**
     * Extracts the first match of a given regex pattern from the input text.
     *
     * @param text     The text to search in.
     * @param regex    The regex pattern to match.
     * @param fallback The fallback value if no match is found.
     * @return The first matched value or the fallback value.
     */
    private String extractUsingRegex(String text, String regex, String fallback) {
        Pattern pattern = Pattern.compile(regex);
        Matcher matcher = pattern.matcher(text);
        return matcher.find() ? matcher.group() : fallback;
    }

    /**
     * Extracts the name from the filename.
     * This method removes the file extension and handles common delimiters like "_", ".", and "-".
     *
     * @param fileName The name of the file.
     * @return Extracted name or "Unknown" if extraction fails.
     */
    private String extractNameFromFileName(String fileName) {
        if (fileName == null || fileName.isEmpty()) {
            return "Unknown";
        }

        // Remove the file extension (e.g., ".pdf", ".doc", etc.)
        String namePart = fileName.replaceAll("\\.[^.]+$", "").trim();

        namePart = namePart.replaceAll("[()\\[]]+", " ").trim();

        // Remove common file-related terms like "resume", "cv", "document", etc.
        namePart = namePart.replaceAll("(?i)(resume|cv|document|profile|project|csv|file|draft|final)", "").trim();

        // Remove additional delimiters like underscores, hyphens, or extra spaces
        namePart = namePart.replaceAll("[_\\-]+", " ").trim();

        // Remove numbers from the name
        namePart = namePart.replaceAll("\\d+", "").trim();

        // If there's still meaningful text left, return it
        if (!namePart.isEmpty()) {
            return namePart;
        }

        // If no valid name is found, return "Unknown"
        return "invalid name format";
    }
}
