package sideproject.madeleinelove.service;

import org.springframework.stereotype.Service;
import sideproject.madeleinelove.entity.WhitePost;
import sideproject.madeleinelove.repository.WhitePostRepository;

import java.util.List;

@Service
public class WhitePostService {

    private final WhitePostRepository whitePostRepository;

    public WhitePostService(WhitePostRepository whitePostRepository) {
        this.whitePostRepository = whitePostRepository;
    }

    public List<WhitePost> getAllPosts() {
        return whitePostRepository.findAll();
    }

}
