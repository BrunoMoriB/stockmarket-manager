package com.bolsavalores.models.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.CONFLICT)
public class JaExisteException extends StockmarketException {

    private static final long serialVersionUID = 1L;

    public JaExisteException(String msg) {
        super(msg);
    }

}