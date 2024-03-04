package com.senai.apivsconnect.controllers;

import com.senai.apivsconnect.azure.AzureBlobStorage;
import com.senai.apivsconnect.dtos.LoginDto;
import com.senai.apivsconnect.dtos.LoginImageDto;
import com.senai.apivsconnect.dtos.TokenDto;
import com.senai.apivsconnect.models.UsuarioModel;
import com.senai.apivsconnect.services.FileUploadService;
import com.senai.apivsconnect.services.TokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@RestController
@RequestMapping(value = "/login", produces = {"application/json"})
public class LoginController {
    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private TokenService tokenService;

    @Autowired
    FileUploadService fileUploadService;

    @Autowired
    AzureBlobStorage azureBlobStorage;

//    @PostMapping
    @PostMapping("/normal")
    public ResponseEntity<Object> login(@RequestBody @Valid LoginDto dadosLogin){
        var usernamePassword = new UsernamePasswordAuthenticationToken(dadosLogin.email(), dadosLogin.senha());

        var auth = authenticationManager.authenticate(usernamePassword);

        var token = tokenService.gerarToken((UsuarioModel) auth.getPrincipal());

        return ResponseEntity.status(HttpStatus.OK).body(new TokenDto(token));
    }


    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<Object> loginComFoto(@ModelAttribute @Valid LoginImageDto loginDto){

        MultipartFile file = loginDto.imagem();

        String extensaoArquivo = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);

        String nomeArquivo = LocalDateTime.now().format(DateTimeFormatter.ofPattern("ddMMyyyyHHmmss")) + "." + extensaoArquivo;

        var teste = azureBlobStorage.UploadFileToBlob( file, nomeArquivo );

        return ResponseEntity.status(HttpStatus.OK).body( file.getOriginalFilename() );
    }
}
