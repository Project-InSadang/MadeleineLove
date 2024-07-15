package sideproject.madeleinelove.service;

import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import sideproject.madeleinelove.repository.WhitePostRepository;
import sideproject.madeleinelove.dto.WhiteRequestDto;
import sideproject.madeleinelove.entity.WhitePost;

import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class WhitePostService {
    @Autowired
    private WhitePostRepository whitePostRepository;
    private static final AtomicInteger ID_GENERATOR = new AtomicInteger(1);


}