import io.restassured.specification.ResponseSpecification;
import static io.restassured.RestAssured.*;
import static org.hamcrest.Matchers.*;


public class ResponseSpecs {
    public static final ResponseSpecification invalidKeySpec = expect()
            .statusCode(Constants.STATUS_OK)
            .body("success", equalTo(false))
            .body("error.code", equalTo(Constants.KEY_ERROR));

    public static final ResponseSpecification validKeySpec = expect()
            .statusCode(Constants.STATUS_OK)
            .body("success", equalTo(true));

    public static final ResponseSpecification liveFieldsSpec = expect()
            .statusCode(Constants.STATUS_OK)
            .body("success", equalTo(true))
            .body("terms", notNullValue())
            .body("privacy", notNullValue())
            .body("timestamp", notNullValue())
            .body("source", equalTo(Constants.CURRENCY.get(0)));

    public static final ResponseSpecification historicalFieldsSpec = expect()
            .statusCode(Constants.STATUS_OK)
            .body("success", equalTo(true))
            .body("terms", notNullValue())
            .body("privacy", notNullValue())
            .body("historical", equalTo(true))
            .body("date", equalTo(Constants.VALID_DATE))
            .body("timestamp", notNullValue())
            .body("source", equalTo(Constants.CURRENCY.get(0)));

    public static final ResponseSpecification invalidEndpointSpec = expect()
            .statusCode(Constants.STATUS_OK)
            .body("success", equalTo(false))
            .body("error.code", equalTo(Constants.INVALID_ENDPOINT_ERROR))
            .body("error.info", equalTo("This API Function does not exist."));

    public static final ResponseSpecification notActiveSpec = expect() // not testing
            .statusCode(Constants.STATUS_OK)
            .body("success", equalTo(false))
            .body("error.code", equalTo(Constants.ACCOUNT_NOT_ACTIVE_ERROR))
            .body("error.info", equalTo("?????"));

    public static final ResponseSpecification illegalRequestSpec = expect()
            .statusCode(Constants.STATUS_OK)
            .body("success", equalTo(false))
            .body("error.code", equalTo(Constants.ILLEGAL_REQUEST_ERROR))
            .body("error.info", containsString("Access Restricted"));

    public static final ResponseSpecification invalidCurrencySpec = expect()
            .statusCode(Constants.STATUS_OK)
            .body("success", equalTo(false))
            .body("error.code", equalTo(Constants.INVALID_CURRENCY_ERROR))
            .body("error.info", containsString(Constants.INVALID_CURRENCY_TYPE));

    public static final ResponseSpecification invalidSourceCurrencySpec = expect()
            .statusCode(Constants.STATUS_OK)
            .body("success", equalTo(false))
            .body("error.code", equalTo(Constants.INVALID_SOURCE_CURRENCY_ERROR))
            .body("error.info", containsString(Constants.INVALID_SOURCE_CURRENCY_TYPE));

    public static final ResponseSpecification historicalNoDateSpec = expect()
            .statusCode(Constants.STATUS_OK)
            .body("success", equalTo(false))
            .body("error.code", equalTo(Constants.NO_DATE_ERROR))
            .body("error.info", containsString(Constants.NO_DATE_TYPE));

    public static final ResponseSpecification historicalInvalidDateSpec = expect()
            .statusCode(Constants.STATUS_OK)
            .body("success", equalTo(false))
            .body("error.code", equalTo(Constants.INVALID_DATE_ERROR))
            .body("error.info", containsString(Constants.INVALID_DATE_TYPE));

}
