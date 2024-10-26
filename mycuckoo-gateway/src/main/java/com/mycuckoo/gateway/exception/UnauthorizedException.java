package com.mycuckoo.gateway.exception;

import com.mycuckoo.core.exception.MyCuckooException;

/**
 * @author rutine
 * @date 2024/8/21 15:34
 */
public class UnauthorizedException extends MyCuckooException {

    public UnauthorizedException() {}

    public UnauthorizedException(String mesg) {
        super(mesg);
    }
}
