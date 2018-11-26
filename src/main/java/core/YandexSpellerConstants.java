package core;

public class YandexSpellerConstants {

    //useful constants for API under test
    public static final String YANDEX_SPELLER_API_URI_PREFIX = "https://speller.yandex.net/services/spellservice.json/";
    public static final String YANDER_SPELLER_CHECKTEXT_URI = YANDEX_SPELLER_API_URI_PREFIX + "checkText";
    public static final String YANDER_SPELLER_CHECKTEXTS_URI = YANDEX_SPELLER_API_URI_PREFIX + "checkTexts";
    public static final String PARAM_TEXT = "text";
    public static final String PARAM_OPTIONS = "options";
    public static final String PARAM_LANG = "lang";
    public static final String PARAM_FORMAT = "format";
    public static final String QUOTES = "\"";

    // Option constants
    // Not as an enum because we use not their string representation, but rather them as digits. We even add them.
    public static final Integer OP_NONE = 0;
    public static final Integer OP_IGNORE_DIGITS = 2;
    public static final Integer OP_IGNORE_URLS = 4;
    public static final Integer OP_FIND_REPEAT_WORDS = 8;
    public static final Integer OP_IGNORE_CAPITALIZATION = 512;
    public static final Integer OP_ALL = OP_IGNORE_DIGITS+OP_IGNORE_URLS+OP_FIND_REPEAT_WORDS+OP_IGNORE_CAPITALIZATION;

    public enum Language {
        RU("ru"),
        UK("uk"),
        EN("en");
        private String languageCode;
        public String langCode(){return languageCode;}

        private Language(String lang) {
            this.languageCode = lang;
        }
    }

    public enum Formats {
        PLAIN(""),
        HTML("html");

        private String format;

        public String getFormat() {
            return format;
        }

        private Formats(String format) {
            this.format = format;
        }
    }

    public enum ErrorCodes {
        ERROR_UNKNOWN_WORD(1),
        ERROR_REPEAT_WORD(2),
        ERROR_CAPITALIZATION(3),
        ERROR_TOO_MANY_ERRORS(4);

        private Integer code;

        public Integer getCode() {
            return code;
        }

        private ErrorCodes(int code) {
            this.code = code;
        }
    }

    public enum SoapAction {
        CHECK_TEXT("checkText", "CheckTextRequest"),
        CHECK_TEXTS("checkTexts", "CheckTextsRequest");
        String method;
        String reqName;

        private SoapAction(String action, String reqName) {
            this.method = action;
            this.reqName = reqName;
        }
    }
}
