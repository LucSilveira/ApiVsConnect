package com.senai.apivsconnect.dtos;

import org.springframework.web.multipart.MultipartFile;

public record LoginImageDto(
        MultipartFile imagem
) {
}
