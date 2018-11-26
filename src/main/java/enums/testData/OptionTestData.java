package enums.testData;

import org.testng.annotations.DataProvider;

import static core.YandexSpellerConstants.ErrorCodes.*;
import static core.YandexSpellerConstants.*;

public class OptionTestData {
    @DataProvider(name = "optionCheckTest")
    public static Object[][] optionCheckProvider() {
        return new Object[][] {
                // String text, String wrongWord, String rightWord,
                // Integer lenientOption, Integer strictOption, Integer errorCode
                {"There should should be a mistake somewhere.", "should", "",
                        OP_NONE, OP_FIND_REPEAT_WORDS, ERROR_REPEAT_WORD.getCode()},
                {"Don't do that, LeeroyJenkins1992!", "LeeroyJenkins1992", "Leeroy Jenkins 1992",
                        OP_IGNORE_DIGITS, OP_NONE, ERROR_UNKNOWN_WORD.getCode()},
                {"Click Here -> https://totallynotfishing.io/", "totallynotfishing", "",
                        OP_IGNORE_URLS, OP_NONE, ERROR_UNKNOWN_WORD.getCode()},
                {"sHOUTING is not permitted", "sHOUTING", "shouting",
                        OP_IGNORE_CAPITALIZATION, OP_NONE, ERROR_CAPITALIZATION.getCode()}
        };
    }

    public static final String COMB_TEXT = "emily (emi38s) has has found a bug at https://yandex.ru";

    @DataProvider(name = "optionCombinationTest")
    public static Object[][] optionCombinationProvider() {
        return new Object[][] {
                /* text, options, wrongWord, errorCode
                 * Only one case needs suggestion, but checking the error code would be enough
                 */
                {COMB_TEXT, "emily", OP_IGNORE_DIGITS+OP_IGNORE_URLS, ERROR_CAPITALIZATION.getCode()},
                {COMB_TEXT, "ru", OP_IGNORE_DIGITS+OP_IGNORE_CAPITALIZATION, ERROR_UNKNOWN_WORD.getCode()},
                {COMB_TEXT, "emi38s", OP_IGNORE_URLS+OP_IGNORE_CAPITALIZATION, ERROR_UNKNOWN_WORD.getCode()},
                {COMB_TEXT, "has", OP_ALL, ERROR_REPEAT_WORD.getCode()}
        };
    }

}
