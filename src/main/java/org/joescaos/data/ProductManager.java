package org.joescaos.data;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

public class ProductManager {

  //private Product product;
  // private Review[] reviews = new Review[5];
  Map<Product, List<Review>> products = new HashMap<>();
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
    Product product = new Food(id, name, price, rating, bestBefore);
    products.putIfAbsent(product, new ArrayList<>());
    return product;
  }

  public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
    Product product = new Drink(id, name, price, rating);
    products.putIfAbsent(product, new ArrayList<>());
    return product;
  }

  public Product reviewProduct(Product product, Rating rating, String comments) {
    List<Review> reviews = products.get(product);
    products.remove(product, reviews);
    reviews.add(new Review(rating, comments));
    int sum = 0;
    for (Review review: reviews) {
      sum += review.rating().ordinal();
    }
    product = product.applyRating(Rateable.convert(Math.round((float) sum / reviews.size())));
    products.put(product, reviews);
    return product;
  }

  public Product reviewProduct(int id, Rating rating, String comments) {
    return reviewProduct(findProductById(id),rating, comments);
  }

  public void printProductReport(Product product) {
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
    List<Review> reviews = products.get(product);
    Collections.sort(reviews);
    for (Review review: reviews) {
        txt.append(MessageFormat.format(resourceBundle.getString("review"),
                review.rating().getStars(),
                review.comments()));
      txt.append('\n');

    }
    if (reviews.isEmpty()) {
      txt.append(resourceBundle.getString("no.reviews"));
      txt.append('\n');
    }
    System.out.println(txt);
  }

  public void printProductReport(int id) {
    printProductReport(findProductById(id));
  }

  public Product findProductById(int id) {
    Product result = null;
    for (Product product: products.keySet()) {
      if (product.getId() == id) {
        result = product;
        break;
      }
    }
    return result;
  }
}
