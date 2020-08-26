package com.bolsavalores.global;

import org.springframework.dao.DataIntegrityViolationException;

public interface Erros {

    public static boolean ehViolacaoDeRegistroUnico(DataIntegrityViolationException ex) {        
        if (ex.getMostSpecificCause().getMessage().contains("duplicate key value violates unique constraint"))
            return true;
        return false;
    }
}