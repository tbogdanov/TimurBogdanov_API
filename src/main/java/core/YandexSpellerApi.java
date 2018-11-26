package core;

import beans.YandexSpellerAnswer;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import io.restassured.RestAssured;
import io.restassured.builder.RequestSpecBuilder;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import io.restassured.specification.RequestSpecification;
import io.restassured.specification.ResponseSpecification;
import org.apache.http.HttpStatus;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

import static core.YandexSpellerConstants.*;
import static core.YandexSpellerConstants.Formats.PLAIN;
import static core.YandexSpellerConstants.Language.EN;
import static org.hamcrest.Matchers.lessThan;

public class YandexSpellerApi {


    //builder pattern
    private YandexSpellerApi() {
    }
    private HashMap<String, Object> params = new HashMap<>();

    public static class ApiBuilder {
        YandexSpellerApi spellerApi;

        private ApiBuilder(YandexSpellerApi gcApi) {
            spellerApi = gcApi;
        }

        public ApiBuilder text(String... text) {
            spellerApi.params.put(PARAM_TEXT, Arrays.asList(text));
            return this;
        }

        public ApiBuilder defaultAttrbutes() {
            return this
                    .options(OP_NONE)
                    .language(EN)
                    .format(PLAIN);
        }

        public ApiBuilder options(Integer options) {
            spellerApi.params.put(PARAM_OPTIONS, options.toString());
            return this;
        }

        public ApiBuilder language(Language... languages) {
            List<String> codeList = Arrays.stream(languages)
                    .map(Language::langCode)
                    .collect(Collectors.toList());
            spellerApi.params.put(PARAM_LANG, String.join(",", codeList));
            return this;
        }

        public ApiBuilder format(Formats format) {
            spellerApi.params.put(PARAM_FORMAT, format.getFormat());
            return this;
        }

        public Response callCheckText() {
            return RestAssured.with()
                    .queryParams(spellerApi.params)
                    .log().all()
                    .get(YANDER_SPELLER_CHECKTEXT_URI).prettyPeek();
        }

        public Response callCheckTexts() {
            return RestAssured.with()
                    .queryParams(spellerApi.params)
                    .log().all()
                    .get(YANDER_SPELLER_CHECKTEXTS_URI).prettyPeek();
        }
    }

    public static ApiBuilder with() {
        YandexSpellerApi api = new YandexSpellerApi();
        return new ApiBuilder(api);
    }


    //get ready Speller answers list form api response
    /*public static List<YandexSpellerAnswer> getYandexSpellerAnswers(Response response){
        return new Gson().fromJson( response.asString().trim(), new TypeToken<List<YandexSpellerAnswer>>() {}.getType());
    }*/

    public static List<List<YandexSpellerAnswer>> getYandexSpellerAnswers(Response response) {
        return new Gson().fromJson( response.asString().trim(), new TypeToken<List<List<YandexSpellerAnswer>>>() {}.getType());
    }

    //set base request and response specifications tu use in tests
    public static ResponseSpecification successResponse(){
        return new ResponseSpecBuilder()
                .expectContentType(ContentType.JSON)
                .expectHeader("Connection", "keep-alive")
                .expectResponseTime(lessThan(20000L))
                .expectStatusCode(HttpStatus.SC_OK)
                .build();
    }

    public static RequestSpecification baseRequestConfiguration(){
        return new RequestSpecBuilder()
                .setAccept(ContentType.XML)
                .setRelaxedHTTPSValidation()
                .setBaseUri(YANDER_SPELLER_CHECKTEXTS_URI)
                .build();
    }
}
