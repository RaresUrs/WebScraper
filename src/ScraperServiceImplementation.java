import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScraperServiceImplementation implements ScraperServiceInterface {

    @Override
    public String scrap(String url) {
        Scraper scraper = new Scraper(url);
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

                String urlToVisit = item
                        .findFirst("<div class=\"productInfo\"")
                        .findFirst("<div class=\"productNameAndPromotions\"")
                        .findFirst("<h3>")
                        .findFirst("<a href").outerHTML();

                String price = item
                        .findFirst("<div class=\"addToTrolleytabBox\"")
                        .findFirst("<div class=\"addToTrolleytabContainer addItemBorderTop\">\n")
                        .findFirst("<div class=\"pricingAndTrolleyOptions\">\n")
                        .findFirst("<div class=")
                        .findFirst("<div class=\"pricing\">\n")
                        .findFirst("<p class=\"pricePerUnit\">\n")
                        .getText();

                userAgent.visit(urlStrip(urlToVisit)).findFirst("<div class");
            }

        } catch (JauntException e) {
            System.err.println(e);
        }
        return "Hello";
    }

    private String removeWhiteSpaces(String string) {
        return string.trim().replaceAll("\\s+", " ");
    }

    private String urlStrip(String url) {
        final Pattern pattern = Pattern.compile("<a href=\"(.+?)\">");
        final Matcher matcher = pattern.matcher(url);
        matcher.find();
        return matcher.group(1);
    }
}
