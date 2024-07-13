package sideproject.madeleinelove.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.dao.BlackPostRepository;
import sideproject.madeleinelove.domain.BlackPost;

@Service
public class BlackPostService {

    @Autowired
    private BlackPostRepository blackPostRepository;

    public BlackPost save(BlackPost blackPost) {
        return blackPostRepository.save(blackPost);
    }

}
