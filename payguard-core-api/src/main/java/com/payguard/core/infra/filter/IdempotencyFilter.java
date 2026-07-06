package com.payguard.core.infra.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.concurrent.TimeUnit;

@Component
public class IdempotencyFilter extends OncePerRequestFilter {

    private static final String IDEMPOTENCY_HEADER = "X-Idempotency-Key";
    private static final String REDIS_PREFIX = "idempotency:";

    private final StringRedisTemplate redisTemplate;

    public IdempotencyFilter(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        return ! "POST".equalsIgnoreCase(request.getMethod());
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String idempotencyKey = request.getHeader(IDEMPOTENCY_HEADER);

        if (idempotencyKey == null || idempotencyKey.isBlank()) {
            enviarErroHeaderAusente(response);
            return;
        }

        String redisKey = REDIS_PREFIX + idempotencyKey;

        Boolean chaveInseridaComSucesso = redisTemplate.opsForValue()
                .setIfAbsent(redisKey, "PEDING",24, TimeUnit.HOURS);

        if(Boolean.FALSE.equals(chaveInseridaComSucesso)){
            String valorCacheado = redisTemplate.opsForValue().get(redisKey);

            if("PEDING".equals(valorCacheado)){
                enviarErroProcessamentoEmAndamento(response);
            } else{
                enviarRespostaCacheada(response, valorCacheado);
            }
            return;
        }

        try {
            filterChain.doFilter(request, response);
        } catch (Exception ex){
            redisTemplate.delete(redisKey);
            throw ex;
        }

        filterChain.doFilter(request, response);
    }

    private void enviarErroHeaderAusente(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.BAD_REQUEST.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"O header obrigatório 'X-Idempotency-Key' está ausente.\"}");
    }

    private void enviarErroProcessamentoEmAndamento(HttpServletResponse response) throws IOException {
        response.setStatus(HttpStatus.CONFLICT.value()); // 409 Conflict
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write("{\"error\": \"Uma requisição com esta mesma chave de idempotência já está sendo processada.\"}");
    }

    private void enviarRespostaCacheada(HttpServletResponse response, String jsonCacheado) throws IOException {
        response.setStatus(HttpStatus.OK.value()); // 200 OK
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(jsonCacheado);
    }
}