package org.joescaos.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.Locale;
import java.util.ResourceBundle;

public class ProductManager {

  private Product product;
  private Review review;
  private Locale locale;
  private ResourceBundle resourceBundle;
  private DateTimeFormatter dateFormater;
  private NumberFormat moneyFormat;

  public ProductManager(Locale locale) {
    this.locale = locale;
    resourceBundle = ResourceBundle.getBundle("resources", locale);
    dateFormater = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
            .localizedBy(locale);
    moneyFormat = NumberFormat.getCurrencyInstance(locale);
  }

  public Product createProduct(
      int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
    product = new Food(id, name, price, rating, bestBefore);
    return product;
  }

  public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
    product = new Drink(id, name, price, rating);
    return product;
  }

  public Product reviewProduct(Product product, Rating rating, String comments) {
    review = new Review(rating, comments);
    this.product = product.applyRating(rating);
    return this.product;
  }

  public void printProductReport() {
    StringBuilder txt = new StringBuilder();
    String type = switch (product) {
      case Food _ -> resourceBundle.getString("food");
      case Drink _ -> resourceBundle.getString("drink");
    };
    txt.append(MessageFormat.format(resourceBundle.getString("product"),
            product.getName(),
            moneyFormat.format(product.getPrice()),
            product.getRating().getStars(),
            dateFormater.format(product.getBestBefore()),
            type));
    txt.append('\n');
    if (review != null) {
      txt.append(MessageFormat.format(resourceBundle.getString("review"),
              review.rating().getStars(),
              review.comments()));
    }
    else {
      txt.append(resourceBundle.getString("no.reviews"));
    }
    txt.append('\n');
    System.out.println(txt);
  }
}
