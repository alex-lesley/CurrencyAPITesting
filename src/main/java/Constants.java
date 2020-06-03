import java.util.ArrayList;

public class Constants {
    public static final String ROOT = "http://api.currencylayer.com";
    public static final String INVALID_RESOURCE = "http://currencylayer.com/live?access_key=cf392f5369d9affccc669d564701acaa";

    public static final String ACCESS_KEY = "cf392f5369d9affccc669d564701acaa";
    public static final String INVALID_KEY = "cf392f5369d9affccc669d564701";

    public static final String VALID_DATE = "2011-01-01";
    public static final String INVALID_DATE = "20110101";
    public static final String FUTURE_DATE = "2021-01-01";

    public static final String INVALID_CURRENCY = "WER";
    public static final String CURRENCY_LIST = "CAD,EUR,ILS,RUB,USD";
    public static final String CURRENCY_LIST_WITH_INVALID_ONE = "CAD,EUR,ILS,RUB,USD,WER";
    public static final ArrayList<String> CURRENCY = new ArrayList<>();

    public static final int STATUS_OK = 200;
    public static final int INVALID_RESOURCE_CODE = 404;
    public static final int KEY_ERROR = 101;
    public static final int INVALID_ENDPOINT_ERROR = 103;
    public static final int ILLEGAL_REQUEST_ERROR = 105;
    public static final int ACCOUNT_NOT_ACTIVE_ERROR = 102;
    public static final int INVALID_CURRENCY_ERROR = 202;
    public static final int INVALID_SOURCE_CURRENCY_ERROR = 201;
    public static final int NO_DATE_ERROR = 301;
    public static final int INVALID_DATE_ERROR = 302;

    public static final String MISSING_KEY_ERROR_TYPE = "missing_access_key";
    public static final String INVALID_KEY_ERROR_TYPE = "invalid_access_key";
    public static final String INVALID_CURRENCY_TYPE = "invalid Currency Codes";
    public static final String INVALID_SOURCE_CURRENCY_TYPE = "invalid Source Currency";
    public static final String NO_DATE_TYPE = "You have not specified a date";
    public static final String INVALID_DATE_TYPE = "You have entered an invalid date";

}
