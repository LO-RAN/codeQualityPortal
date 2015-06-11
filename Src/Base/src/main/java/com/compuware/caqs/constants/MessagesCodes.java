package com.compuware.caqs.constants;

/**
 *
 * @author cwfr-dzysman
 */
public enum MessagesCodes {
    NO_ERROR("", MessagesTypes.INFO),
    MODEL_DUPLICATION_MODEL_ALREADY_EXISTS("modelDuplicationModelAlreadyExists", MessagesTypes.ERROR),
    MODEL_DUPLICATION_ERROR("modelDuplicationError", MessagesTypes.ERROR),
    MODEL_DELETION_ERROR("modelDeletionError", MessagesTypes.ERROR),
    CONFIGURATION_ERROR("configurationError", MessagesTypes.ERROR),
    ATTACHED_BASELINE("attachedBL", MessagesTypes.ERROR),
    CAQS_GENERIC_ERROR("caqsGenericError", MessagesTypes.ERROR),
    DATABASE_ERROR("databaseError", MessagesTypes.ERROR),
    CREATE_SYMLINK_ERROR("createSymLinkError", MessagesTypes.ERROR),
    DELETE_SYMLINK_ERROR("deleteSymLinkError", MessagesTypes.ERROR),
    SYMLINK_ALREADY_EXISTS("symLinkAlreadyExists", MessagesTypes.ERROR),
    DELETE_PEREMPTED_ELEMENT_ERROR("deletePeremptedError", MessagesTypes.ERROR),
    ERROR_DURING_FILE_UPLOAD("errorDuringFileUpload", MessagesTypes.ERROR),
    DISCONNECT("sessionTerminated", MessagesTypes.DISCONNECT);

    private static final String RESOURCES_CODE_PREFIX = "caqs.messagescode.";

    MessagesCodes(String c, MessagesTypes m) {
        this.type = m;
        this.code = c;
    }

    private MessagesTypes type;
    private String code;

    public String getCode() {
        return RESOURCES_CODE_PREFIX + this.code;
    }

    public MessagesTypes getType() {
        return this.type;
    }
}
