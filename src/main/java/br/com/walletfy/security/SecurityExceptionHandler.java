package br.com.walletfy.security;

import java.io.IOException;
import java.time.LocalDateTime;

import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import br.com.walletfy.dto.ErrorResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import tools.jackson.databind.ObjectMapper;

@Component
@RequiredArgsConstructor
public class SecurityExceptionHandler implements AuthenticationEntryPoint, AccessDeniedHandler {

    private final ObjectMapper objectMapper;

    // 401 - token inválido/expirado
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response,
            AuthenticationException ex) throws IOException {

        String mensagem = "Token inválido ou expirado";
        if (ex.getCause() instanceof ExpiredJwtException) {
            mensagem = "Token expirado, faça login novamente";
        }

        escreverResposta(response, HttpServletResponse.SC_UNAUTHORIZED, mensagem);
    }

    // 403 - acesso negado
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response,
            AccessDeniedException ex) throws IOException {
        escreverResposta(response, HttpServletResponse.SC_FORBIDDEN, "Acesso negado");
    }

    private void escreverResposta(HttpServletResponse response, int status, String mensagem) throws IOException {
        ErrorResponseDTO erro = ErrorResponseDTO.builder()
                .status(status)
                .mensagem(mensagem)
                .dataHora(LocalDateTime.now())
                .build();

        response.setStatus(status);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(erro));
    }

}
