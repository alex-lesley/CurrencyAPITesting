import io.restassured.RestAssured;
import io.restassured.builder.ResponseSpecBuilder;
import io.restassured.response.Response;
import io.restassured.specification.ResponseSpecification;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;
import io.restassured.filter.log.RequestLoggingFilter;


public class NewCurrencyTest {
    private final Logger logger = LogManager.getLogger(NewCurrencyTest.class);

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
        logger.info("Endpoint /live, No access key test");
        given().expect().spec(ResponseSpecs.invalidKeySpec)
                .when().get(Endpoints.live,"")
                .then().body("error.type", equalTo(Constants.MISSING_KEY_ERROR_TYPE));
    }

    @Test
    public void invalidAccessKeyTest() {
        logger.info("Endpoint /live, Invalid access key test");
        given().expect().spec(ResponseSpecs.invalidKeySpec)
                .when().get(Endpoints.live, Constants.INVALID_KEY)
                .then().body("error.type", equalTo(Constants.INVALID_KEY_ERROR_TYPE));
    }

    @Test
    public void responseBodyLiveFieldsTest() {
        logger.info("Endpoint /live, Fields in response body test");
        given().expect().spec(ResponseSpecs.liveFieldsSpec)
                .when().get(Endpoints.live, Constants.ACCESS_KEY)
                .then().body(String.format("quotes.%s%s", Constants.CURRENCY.get(0), Constants.CURRENCY.get(1)), notNullValue());
    }

    @Test
    public void responseLinksTest() {
        logger.info("Endpoint /live, Terms and Privacy links test");
        Response response = given().get(Endpoints.live, Constants.ACCESS_KEY);
        String terms = response.then().body(notNullValue()).extract().path("terms");
        String privacy = response.then().body(notNullValue()).extract().path("privacy");
        given().get(terms).then().statusCode(Constants.STATUS_OK);
        given().get(privacy).then().statusCode(Constants.STATUS_OK);
    }

    @Test
    public void liveOneCurrencyTest() {
        logger.info("Endpoint /live with one currency parameter test");
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
        logger.info("Endpoint /live with currencies list test");
        Response response = given().expect().spec(ResponseSpecs.liveFieldsSpec)
                .when().get(Endpoints.liveCurrency, Constants.ACCESS_KEY, Constants.CURRENCY_LIST);
        for (String currency : Constants.CURRENCY) {
            response.then().body(String.format("quotes.%s%s", Constants.CURRENCY.get(0), currency), notNullValue());
        }
    }

    @Test
    public void responseBodyHistoricalFieldsTest() {
        logger.info("Endpoint /historical, Fields in response body test");
        given().expect().spec(ResponseSpecs.historicalFieldsSpec)
                .when().get(Endpoints.historical, Constants.ACCESS_KEY, Constants.VALID_DATE)
                .then().body(String.format("quotes.%s%s", Constants.CURRENCY.get(0), Constants.CURRENCY.get(1)), notNullValue());
    }

    @Test
    public void historicalOneCurrencyTest() {
        logger.info("Endpoint /historical, one currency parameter test");
        for (String currency : Constants.CURRENCY) {
            Response response = given().expect().spec(ResponseSpecs.historicalFieldsSpec)
                    .when().get(Endpoints.historicalCurrency, Constants.ACCESS_KEY, Constants.VALID_DATE, currency);
            response.then().body("source", equalTo(Constants.CURRENCY.get(0)))
                    .and().body(String.format("quotes.%s%s", Constants.CURRENCY.get(0), currency), notNullValue());
        }
    }

    @Test
    public void historicalCurrenciesListTest() {
        logger.info("Endpoint /historical, currencies list test");
        Response response = given().expect().spec(ResponseSpecs.historicalFieldsSpec)
                .when().get(Endpoints.historicalCurrency, Constants.ACCESS_KEY, Constants.VALID_DATE, Constants.CURRENCY_LIST);
        for (String currency : Constants.CURRENCY) {
            response.then().body(String.format("quotes.%s%s", Constants.CURRENCY.get(0), currency), notNullValue());
        }
    }

    @Test
    public void invalidResourceTest() {
        logger.info("Invalid resource test");
        given().get(Constants.INVALID_RESOURCE)
                .then().statusCode(Constants.INVALID_RESOURCE_CODE);
    }

    @Test
    public void invalidEndpointTest() {
        logger.info("Invalid endpoint test");
        given().expect().spec(ResponseSpecs.invalidEndpointSpec)
                .when().get(Endpoints.invalidEndpoint, Constants.ACCESS_KEY);
    }

// not testing errors 102 and 104 - the limit of requests is not reached
//    @Test
//    public void accountNotActiveTest() {
//    logger.info("Out of account limit test");
//    Response response = given().log().all().expect().spec(ResponseSpecs.notActiveSpec)
//                .when().get(Endpoints.live, Constants.ACCESS_KEY);
//    response.body().prettyPrint();
//    }

    @Test
    public void illegalEndpointTest() { // out of subscription plan
        logger.info("Endpoint out of subscription plan test");
        given().expect().spec(ResponseSpecs.illegalRequestSpec)
                .when().get(Endpoints.illegalEndpoint, Constants.ACCESS_KEY);
    }

    @Test
    public void illegalSourceParameterLiveTest() {  // out of subscription plan
        logger.info("Parameter out of subscription plan test");
        given().expect().spec(ResponseSpecs.illegalRequestSpec)
                .when().get(Endpoints.liveSource, Constants.ACCESS_KEY, Constants.CURRENCY.get(1), Constants.CURRENCY_LIST);
    }

    @Test
    public void invalidCurrencyCodeLiveTest() {
        logger.info("Invalid currency code test");
        given().expect().spec(ResponseSpecs.invalidCurrencySpec)
                .when().get(Endpoints.liveCurrency, Constants.ACCESS_KEY, Constants.INVALID_CURRENCY);
    }

    @Test
    public void validAndInvalidCurrencyCodesLiveTest() {
        logger.info("One invalid code in currencies list test");
        Response response = given().expect().spec(ResponseSpecs.liveFieldsSpec)
                .when().get(Endpoints.liveCurrency, Constants.ACCESS_KEY, Constants.CURRENCY_LIST_WITH_INVALID_ONE);
        for (String currency : Constants.CURRENCY) {
            response.then().body(String.format("quotes.%s%s", Constants.CURRENCY.get(0), currency), notNullValue());
        }
    }

    @Test
    public void invalidSourceParameterLiveTest() {
        logger.info("Invalid source currency test");
        given().expect().spec(ResponseSpecs.invalidSourceCurrencySpec)
                .when().get(Endpoints.liveSource, Constants.ACCESS_KEY, Constants.INVALID_CURRENCY, Constants.CURRENCY_LIST);
    }

    @Test
    public void historicalNoDateTest() {
        logger.info("Endpoint /historical with no date test");
        given().expect().spec(ResponseSpecs.historicalNoDateSpec)
                .when().get(Endpoints.historical, Constants.ACCESS_KEY, "");
    }

    @Test
    public void historicalInvalidDateTest() {
        logger.info("Endpoint /historical with invalid date test");
        given().expect().spec(ResponseSpecs.historicalInvalidDateSpec)
                .when().get(Endpoints.historical, Constants.ACCESS_KEY, Constants.INVALID_DATE);
    }

    @Test
    public void historicalFutureDateTest() {
        logger.info("Endpoint /historical with future date test");
        given().expect().spec(ResponseSpecs.historicalInvalidDateSpec)
                .when().get(Endpoints.historical, Constants.ACCESS_KEY, Constants.FUTURE_DATE);
    }
}
