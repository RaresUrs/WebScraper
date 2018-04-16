import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

import java.util.HashSet;

public class ScraperServiceImplementation implements ScraperServiceInterface {

    @Override
    public String scrap(String url) {
        Scraper scraper = new Scraper(url);
        HashSet<String> elements = new HashSet<>();

        try {
            UserAgent userAgent = new UserAgent();
            userAgent.visit(scraper.getUrlToScrap());
            Elements items = userAgent.doc.findEach("<li class=\"gridItem\"");
            for (Element item : items) {

                String title = item
                        .findFirst("<div class=\"productInfo\"")
                        .findFirst("<div class=\"productNameAndPromotions\"")
                        .findFirst("<h3>")
                        .findFirst("<a href")
                        .getText();

                String price = item
                        .findFirst("<div class=\"addToTrolleytabBox\"")
                        .findFirst("<div class=\"addToTrolleytabContainer addItemBorderTop\">\n")
                        .findFirst("<div class=\"pricingAndTrolleyOptions\">\n")
                        .findFirst("<div class=")
                        .findFirst("<div class=\"pricing\">\n")
                        .findFirst("<p class=\"pricePerUnit\">\n")
                        .getText();

                elements.add(title.trim().replaceAll("\\s+", " "));
                elements.add(price.trim().replaceAll("\\s+", " "));
            }
        } catch (JauntException e) {
            System.err.println(e);
        }
        return "Hello";
    }
}
