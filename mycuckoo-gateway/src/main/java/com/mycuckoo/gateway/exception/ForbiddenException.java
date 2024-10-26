package com.mycuckoo.gateway.exception;

import com.mycuckoo.core.exception.MyCuckooException;

/**
 * @author rutine
 * @date 2024/8/21 15:34
 */
public class ForbiddenException extends MyCuckooException {

    public ForbiddenException() {}

    public ForbiddenException(String mesg) {
        super(mesg);
    }
}
