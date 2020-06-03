public class Endpoints {
    public static final String live = "/live?access_key={key}";
    public static final String liveCurrency = "/live?access_key={key}&currencies={currency}";
    public static final String liveSource = "/live?access_key={key}&source={source}&currencies={currency}";
    public static final String historical = "/historical?access_key={key}&date={date}";
    public static final String historicalCurrency = "/historical?access_key={key}&date={date}&currencies={currency}";
    public static final String invalidEndpoint = "/leave?access_key={key}";
    public static final String illegalEndpoint = "/convert?access_key={key}&from=GBP&to=USD&amount=10";
}
