import com.jaunt.Element;
import com.jaunt.Elements;
import com.jaunt.JauntException;
import com.jaunt.UserAgent;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ScraperServiceImplementation implements ScraperServiceInterface {

    @Override
    public String scrap(String url) {
        Scraper scraper = new Scraper(url);
        ProductDetails productDetails = new ProductDetails();
        List<ProductDetails> products = new ArrayList<>();

        try {
            UserAgent userAgent = new UserAgent();
            userAgent.visit(scraper.getUrlToScrap());
            Elements items = userAgent.doc.findFirst("<ul class=\"productLister gridView\"").findEach("<li class=\"gridItem\"");
            double totalPrice = 0;
            for (Element item : items) {
                try {
                    String title = item
                            .findFirst("<div class=\"productInfo\"")
                            .findFirst("<div class=\"productNameAndPromotions\"")
                            .findFirst("<h3>")
                            .findFirst("<a href")
                            .getText()
                            .trim();

                    String price = item
                            .findFirst("<div class=\"addToTrolleytabBox\"")
                            .findFirst("<div class=\"addToTrolleytabContainer addItemBorderTop\">\n")
                            .findFirst("<div class=\"pricingAndTrolleyOptions\">\n")
                            .findFirst("<div class=")
                            .findFirst("<div class=\"pricing\">\n")
                            .findFirst("<p class=\"pricePerUnit\">\n")
                            .getText()
                            .trim();


                    String urlToVisit = item
                            .findFirst("<div class=\"productInfo\"")
                            .findFirst("<div class=\"productNameAndPromotions\"")
                            .findFirst("<h3>")
                            .findFirst("<a href").outerHTML();

                    String description = userAgent.
                            visit(urlStrip(urlToVisit))
                            .findFirst("<div class=\"section\"")
                            .findFirst("<p>")
                            .getText().trim();

                    Elements cal = userAgent
                            .visit(urlStrip(urlToVisit))
                            .findFirst("<div class=\"tableWrapper\"").findFirst("<table class=\"nutritionTable\"").findEach("<tr class=\"tableRow0\"");

                    price = price.replace("£", "");
                    totalPrice += Double.parseDouble(price);

                    productDetails.setTitle(title);
                    productDetails.setDescription(description);
                    productDetails.setUnitPrice(Double.parseDouble(price));

                    for (Element kcal : cal) {
                        String calDescription = kcal.findFirst("<td").getText();

                        if (calDescription.contains("kcal")) {
                            productDetails.setkCalories(calDescription);
                            //System.out.println(calDescription); // Obiectu
                        } else {
                            productDetails.setkCalories("");
                        }
                    }

                    products.add(productDetails);

                    //System.out.println(calories.trim());
                } catch (JauntException e) {
                    if (e.getMessage().contains("not found: <div class=\"tableWrapper\"")) {
                        productDetails.setkCalories("");
                    } else {
                        System.err.println("There was an error: " + e);
                    }
                }
            }

        } catch (JauntException e) {
            System.err.println("There was an error: " + e);
        }
        return "Hello";
    }

    private String urlStrip(String url) {
        final Pattern pattern = Pattern.compile("<a href=\"(.+?)\">");
        final Matcher matcher = pattern.matcher(url);
        matcher.find();
        return matcher.group(1);
    }
}
