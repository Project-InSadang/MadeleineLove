package sideproject.madeleinelove.service;

import jakarta.validation.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;
import sideproject.madeleinelove.dto.BlackPostDto;
import sideproject.madeleinelove.entity.BlackPost;
import sideproject.madeleinelove.repository.BlackPostRepository;

import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@ActiveProfiles("test") // application-test.properties 활성화
@EnableMongoRepositories(basePackageClasses = {BlackPostRepository.class})
public class BlackPostServiceTest {

    @Autowired
    private BlackPostService blackPostService;

    private Validator validator;

    @BeforeEach
    public void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
    }

    @Test
    public void testSaveBlackPost() {
        // Given
        String userId = "user1";
        BlackPostDto blackPostDto = new BlackPostDto();
        blackPostDto.setNickName("레니");
        blackPostDto.setContent("Hello World!");
        blackPostDto.setCleanMethod(1);

        // When
        BlackPost savedPost = blackPostService.save(userId, blackPostDto);

        // Then
        assertThat(savedPost).isNotNull();
        assertThat(savedPost.getPostId()).isNotNull();
        assertThat(savedPost.getUserId()).isEqualTo(userId);
        assertThat(savedPost.getNickName()).isEqualTo(blackPostDto.getNickName());
        assertThat(savedPost.getContent()).isEqualTo(blackPostDto.getContent());
        assertThat(savedPost.getCleanMethod()).isEqualTo(blackPostDto.getCleanMethod());
        assertThat(savedPost.getLikeCount()).isEqualTo(0);
    }

    @Test
    public void testSaveBlackPostWithBlankData() {
        // Given
        String userId = "user1";
        BlackPostDto blackPostDto = new BlackPostDto();
        blackPostDto.setNickName("");
        blackPostDto.setContent("");
        blackPostDto.setCleanMethod(1);

        // When
        ConstraintViolationException thrown = assertThrows(ConstraintViolationException.class, () -> {
            performValidation(blackPostDto);
            blackPostService.save(userId, blackPostDto);
        });

        // Then
        assertThat(thrown.getMessage()).contains("must not be blank");
    }

    private void performValidation(BlackPostDto blackPostDto) {
        Set<ConstraintViolation<BlackPostDto>> violations = validator.validate(blackPostDto);
        if (!violations.isEmpty()) {
            throw new ConstraintViolationException(violations);
        }
    }

}
