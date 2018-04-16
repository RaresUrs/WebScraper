public class SainsburyScraper {
    private static final String URL_TO_SCRAP = "https://jsainsburyplc.github.io/serverside-test/site/" +
            "www.sainsburys.co.uk/webapp/wcs/stores/servlet/gb/groceries/berries-cherries-currants6039.html";

    public static void main(String[] args) {
        ScraperServiceImplementation scraperServiceInterface = new ScraperServiceImplementation();
        scraperServiceInterface.scrap(URL_TO_SCRAP);
    }
}

