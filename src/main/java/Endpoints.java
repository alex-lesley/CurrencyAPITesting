public class Endpoints {
    public static final String live = "/live?access_key={key}";
    public static final String liveCurrency = "/live?access_key={key}&currencies={currency}";
    public static final String illegalLiveParameter = "/live?access_key={key}&source=EUR&currencies={currency}";
    public static final String invalidLiveParameter = "/live?access_key={key}&source=WER&currencies={currency}";
    public static final String historical = "/historical?access_key={key}&date={date}";
    public static final String invalidEndpoint = "/leave?access_key={key}";
    public static final String illegalEndpoint = "/convert?access_key={key}&from=GBP&to=USD&amount=10";
}
