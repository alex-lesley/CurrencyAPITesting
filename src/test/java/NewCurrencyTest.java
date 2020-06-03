import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.filter.log.RequestLoggingFilter;


public class NewCurrencyTest {

    @BeforeAll
    public static void classSetUp() {
        baseURI = Constants.ROOT;
        Constants.CURRENCY.add("USD"); // default currency
        Constants.CURRENCY.add("CAD");
        Constants.CURRENCY.add("EUR");
        Constants.CURRENCY.add("ILS");
        Constants.CURRENCY.add("RUB");
    }

    @Test
    public void noAccessKeyTest() {
        given().expect().spec(ResponseSpecs.invalidKeySpec)
                .when().get(Endpoints.live,"")
                .then().body("error.type", equalTo(Constants.MISSING_KEY_ERROR_TYPE));
    }

    @Test
    public void invalidAccessKeyTest() {
        given().expect().spec(ResponseSpecs.invalidKeySpec)
                .when().get(Endpoints.live, Constants.INVALID_KEY)
                .then().body("error.type", equalTo(Constants.INVALID_KEY_ERROR_TYPE));
    }

    @Test
    public void responseBodyLiveFieldsTest() {
        given().expect().spec(ResponseSpecs.liveFieldsSpec)
                .when().get(Endpoints.live, Constants.ACCESS_KEY)
                .then().body(String.format("quotes.%s%s", Constants.CURRENCY.get(0), Constants.CURRENCY.get(1)), notNullValue());
    }

    @Test
    public void responseLinksTest() {
        Response response = given().get(Endpoints.live, Constants.ACCESS_KEY);
        String terms = response.then().body(notNullValue()).extract().path("terms");
        String privacy = response.then().body(notNullValue()).extract().path("privacy");
        given().get(terms).then().statusCode(Constants.STATUS_OK);
        given().get(privacy).then().statusCode(Constants.STATUS_OK);
    }

    @Test
    public void liveOneCurrencyTest() {
        for (String currency : Constants.CURRENCY) {
            Response response = given().expect().spec(ResponseSpecs.liveFieldsSpec)
                    .when().get(Endpoints.liveCurrency, Constants.ACCESS_KEY, currency);
//            response.body().prettyPrint();
            response.then().body("source", equalTo(Constants.CURRENCY.get(0)))
                    .and().body(String.format("quotes.%s%s", Constants.CURRENCY.get(0), currency), notNullValue());
        }
    }

    @Test
    public void liveCurrenciesListTest() {
        Response response = given().expect().spec(ResponseSpecs.liveFieldsSpec)
                .when().get(Endpoints.liveCurrency, Constants.ACCESS_KEY, Constants.CURRENCY_LIST);
        for (String currency : Constants.CURRENCY) {
            response.then().body(String.format("quotes.%s%s", Constants.CURRENCY.get(0), currency), notNullValue());
        }
    }

    @Test
    public void responseBodyHistoricalFieldsTest() {
        given().expect().spec(ResponseSpecs.historicalFieldsSpec)
                .when().get(Endpoints.historical, Constants.ACCESS_KEY, Constants.VALID_DATE)
                .then().body(String.format("quotes.%s%s", Constants.CURRENCY.get(0), Constants.CURRENCY.get(1)), notNullValue());
    }

    @Test
    public void historicalOneCurrencyTest() {
        for (String currency : Constants.CURRENCY) {
            Response response = given().expect().spec(ResponseSpecs.historicalFieldsSpec)
                    .when().get(Endpoints.historicalCurrency, Constants.ACCESS_KEY, Constants.VALID_DATE, currency);
            response.body().prettyPrint();
            response.then().body("source", equalTo(Constants.CURRENCY.get(0)))
                    .and().body(String.format("quotes.%s%s", Constants.CURRENCY.get(0), currency), notNullValue());
        }
    }

    @Test
    public void historicalCurrenciesListTest() {
        Response response = given().expect().spec(ResponseSpecs.historicalFieldsSpec)
                .when().get(Endpoints.historicalCurrency, Constants.ACCESS_KEY, Constants.VALID_DATE, Constants.CURRENCY_LIST);
        for (String currency : Constants.CURRENCY) {
            response.then().body(String.format("quotes.%s%s", Constants.CURRENCY.get(0), currency), notNullValue());
        }
    }

    @Test
    public void invalidResourceTest() {
        given().get(Constants.INVALID_RESOURCE)
                .then().statusCode(Constants.INVALID_RESOURCE_CODE);
    }

    @Test
    public void invalidEndpointTest() {
        given().expect().spec(ResponseSpecs.invalidEndpointSpec)
                .when().get(Endpoints.invalidEndpoint, Constants.ACCESS_KEY);
    }

// not testing errors 102 and 104 - the limit of requests is not reached
//    @Test
//    public void accountNotActiveTest() {
//    Response response = given().log().all().expect().spec(ResponseSpecs.notActiveSpec)
//                .when().get(Endpoints.live, Constants.ACCESS_KEY);
//    response.body().prettyPrint();
//    }

    @Test
    public void illegalEndpointTest() { // out of subscription plan
        given().expect().spec(ResponseSpecs.illegalRequestSpec)
                .when().get(Endpoints.illegalEndpoint, Constants.ACCESS_KEY);
    }

    @Test
    public void illegalSourceParameterLiveTest() {  // out of subscription plan
        given().expect().spec(ResponseSpecs.illegalRequestSpec)
                .when().get(Endpoints.liveSource, Constants.ACCESS_KEY, Constants.CURRENCY.get(1), Constants.CURRENCY_LIST);
    }

    @Test
    public void invalidCurrencyCodeLiveTest() {
        given().expect().spec(ResponseSpecs.invalidCurrencySpec)
                .when().get(Endpoints.liveCurrency, Constants.ACCESS_KEY, Constants.INVALID_CURRENCY);
    }

    @Test
    public void validAndInvalidCurrencyCodesLiveTest() {
        Response response = given().expect().spec(ResponseSpecs.liveFieldsSpec)
                .when().get(Endpoints.liveCurrency, Constants.ACCESS_KEY, Constants.CURRENCY_LIST_WITH_INVALID_ONE);
        for (String currency : Constants.CURRENCY) {
            response.then().body(String.format("quotes.%s%s", Constants.CURRENCY.get(0), currency), notNullValue());
        }
    }

    @Test
    public void invalidSourceParameterLiveTest() {
        given().expect().spec(ResponseSpecs.invalidSourceCurrencySpec)
                .when().get(Endpoints.liveSource, Constants.ACCESS_KEY, Constants.INVALID_CURRENCY, Constants.CURRENCY_LIST);
    }

    @Test
    public void historicalNoDateTest() {
        given().expect().spec(ResponseSpecs.historicalNoDateSpec)
                .when().get(Endpoints.historical, Constants.ACCESS_KEY, "");
    }

    @Test
    public void historicalInvalidDateTest() {
        given().expect().spec(ResponseSpecs.historicalInvalidDateSpec)
                .when().get(Endpoints.historical, Constants.ACCESS_KEY, Constants.INVALID_DATE);
    }

    @Test
    public void historicalFutureDateTest() {
        given().expect().spec(ResponseSpecs.historicalInvalidDateSpec)
                .when().get(Endpoints.historical, Constants.ACCESS_KEY, Constants.FUTURE_DATE);
    }
}
