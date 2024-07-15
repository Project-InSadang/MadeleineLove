package sideproject.madeleinelove.service;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import sideproject.madeleinelove.dto.BlackPostDto;
import sideproject.madeleinelove.entity.BlackPost;
import sideproject.madeleinelove.repository.BlackPostRepository;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
@ActiveProfiles("test") // application-test.properties 활성화
@EnableMongoRepositories(basePackageClasses = {BlackPostRepository.class})
public class BlackPostServiceTest {

    @Autowired
    private BlackPostRepository blackPostRepository;

    @Autowired
    private BlackPostService blackPostService;

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
        assertThat(savedPost.getLikeCount()).isEqualTo(1);
    }

}
