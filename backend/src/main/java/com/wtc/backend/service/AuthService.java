package com.wtc.backend.service;

import com.wtc.backend.config.JwtUtils;
import com.wtc.backend.dto.LoginRequest;
import com.wtc.backend.dto.LoginResponse;
import com.wtc.backend.dto.OperatorDTO;
import com.wtc.backend.model.Operator;
import com.wtc.backend.repository.OperatorRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthService {

    private final OperatorRepository operatorRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtils jwtUtils;

    public AuthService(OperatorRepository operatorRepository, PasswordEncoder passwordEncoder,
                       AuthenticationManager authenticationManager, JwtUtils jwtUtils) {
        this.operatorRepository = operatorRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtils = jwtUtils;
    }

    public LoginResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getEmail(), request.getSenha())
        );
        Operator operator = operatorRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new RuntimeException("Operador não encontrado"));
        String token = jwtUtils.generateToken(operator.getEmail(), operator.getId());
        return LoginResponse.builder()
                .token(token).tokenType("Bearer").operator(toDTO(operator)).build();
    }

    public boolean emailJaCadastrado(String email) {
        return operatorRepository.findByEmail(email).isPresent();
    }

    public Operator createOperator(String nome, String email, String senha, String cargo) {
        Operator operator = Operator.builder()
                .nome(nome).email(email).senha(passwordEncoder.encode(senha))
                .cargo(cargo).darkMode(true).notas("").build();
        return operatorRepository.save(operator);
    }

    public OperatorDTO toDTO(Operator operator) {
        return OperatorDTO.builder()
                .id(operator.getId()).nome(operator.getNome()).email(operator.getEmail())
                .cargo(operator.getCargo()).avatarUrl(operator.getAvatarUrl())
                .darkMode(operator.isDarkMode()).notas(operator.getNotas()).build();
    }
}
