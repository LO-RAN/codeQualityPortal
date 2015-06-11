package com.compuware.carscode.plugin.deventreprise.util;

/**
 *
 * @author cwfr-dzysman
 */
public enum ReturnCodes {
    NO_ERROR(0),
    SRC_DIR_NOT_ACCESSIBLE(1),
    IMPOSSIBLE_TO_COPY_DOWNLOAD(2),
    DEVENTREPRISE_DOWNLOAD_DIR_NOT_ACCESSIBLE(3),
    ERROR_PROGRAM_ANALYSER(4),
    ERROR_CONNECTING_TO_DATABASE(5),
    ERROR_WRITING_RESULTS(6),
    LEARNING_FAILED(7),
    BAD_PARAMETERS(8),
    LEARNING_PENDING(9),
    NO_CONNECTION(10),
    GENERIC_ERROR(11);

    private int code;

    private ReturnCodes(int c) {
        this.code = c;
    }


    public int getCode() {
        return this.code;
    }
}
