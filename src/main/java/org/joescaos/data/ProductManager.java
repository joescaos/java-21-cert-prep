package org.joescaos.data;

import org.joescaos.exception.ProductManagerException;

import java.math.BigDecimal;
import java.text.MessageFormat;
import java.text.NumberFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.FormatStyle;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;
import java.util.function.Predicate;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

public class ProductManager {

  private static final Logger LOGGER = Logger.getLogger(ProductManager.class.getName());

  private Map<Product, List<Review>> products = new HashMap<>();

  private static final ProductManager pm = new ProductManager();

  private static final Map<String, ResourceFormatter> formatters = Map.of(
          "en-GB", new ResourceFormatter(Locale.UK),
          "en-US", new ResourceFormatter(Locale.US),
          "es-CO", new ResourceFormatter(Locale.of("es", "CO")),
          "fr-FR", new ResourceFormatter(Locale.FRANCE),
          "zh-CN", new ResourceFormatter(Locale.CHINA)
  );

  private ReentrantReadWriteLock lock = new ReentrantReadWriteLock();
  private Lock writeLock = lock.writeLock();
  private Lock readLock = lock.readLock();


  private ProductManager() {

  }

  public static ProductManager getInstance() {
    return pm;
  }



  public Product createProduct(
      int id, String name, BigDecimal price, Rating rating, LocalDate bestBefore) {
    Product product = null;
    try {
      writeLock.lock();
      product = new Food(id, name, price, rating, bestBefore);
      products.putIfAbsent(product, new ArrayList<>());
    } catch (Exception e) {
      LOGGER.log(Level.INFO, "Error adding product "+ e.getMessage());
      return null;
    } finally{
      writeLock.unlock();
    }

    return product;
  }

  public Product createProduct(int id, String name, BigDecimal price, Rating rating) {
    Product product = null;
    try {
      writeLock.lock();
      product = new Drink(id, name, price, rating);
      products.putIfAbsent(product, new ArrayList<>());

    } catch (Exception e) {
      LOGGER.log(Level.INFO, "Error adding product "+ e.getMessage());
      return null;
    } finally{
      writeLock.unlock();
    }
    return product;
  }

  private Product reviewProduct(Product product, Rating rating, String comments) {
    List<Review> reviews = products.get(product);
    products.remove(product, reviews);
    reviews.add(new Review(rating, comments));

    product.applyRating(
            Rateable.convert(
                    (int) Math.round(
                            reviews.stream()
                                    .mapToInt(r -> rating.ordinal())
                                    .average()
                                    .orElse(0))));
    products.put(product, reviews);
    return product;
  }

  public Product reviewProduct(int id, Rating rating, String comments) {
      try {
        writeLock.lock();
        return reviewProduct(findProductById(id),rating, comments);
      } catch (ProductManagerException e) {
          LOGGER.log(Level.INFO, e.getMessage());
          return null;
      } finally{
        writeLock.unlock();
      }
  }

  public void printProductReport(Product product, String languageTag) {
    StringBuilder txt = new StringBuilder();
    ResourceFormatter formatter = changeLocale(languageTag);

    txt.append(formatter.formatProduct(product));
    txt.append('\n');
    List<Review> reviews = products.get(product);
    Collections.sort(reviews);

    if (reviews.isEmpty()) {
      txt.append(formatter.getText("no.reviews"));
      txt.append('\n');
    } else {
      txt.append(reviews.stream()
              .map( r -> formatter.formatReview(r) + "\n")
              .collect(Collectors.joining()));
    }

    System.out.println(txt);
  }

  public void printProductReport(int id, String languageTag) {
      try {
        readLock.lock();
        printProductReport(findProductById(id), languageTag);
      } catch (ProductManagerException e) {
          LOGGER.log(Level.INFO, e.getMessage());
      } finally{
        readLock.unlock();
      }
  }

  private void printProducts(Predicate<Product> predicate, Comparator<Product> sorter, String languageTag) {
    ResourceFormatter formatter = changeLocale(languageTag);
    StringBuilder txt = new StringBuilder();
    txt.append(products.keySet().stream()
            .sorted(sorter)
            .filter(predicate)
            .map(p -> formatter.formatProduct(p) + "\n")
            .collect(Collectors.joining()));

    System.out.println(txt);
  }

  public Product findProductById(int id) throws ProductManagerException {
    try{
      readLock.lock();
      return products.keySet()
              .stream()
              .filter(product -> product.getId() == id)
              .findFirst()
              .orElseThrow(() ->
                      new ProductManagerException("Product with "+ id + " Not Found"));
    } finally {
      readLock.unlock();

    }

  }

  public ResourceFormatter changeLocale(String languageTag) {
    return formatters.getOrDefault(languageTag, formatters.get("en-US"));
  }

  public static Set<String> getSupportedLocales() {
    return formatters.keySet();
  }

  public Map<String, String> getDiscounts(String languageTag) {
    try {
      readLock.lock();
      ResourceFormatter formatter = changeLocale(languageTag);
      return products.keySet().stream()
              .collect(Collectors.groupingBy(
                      product -> product.getRating().getStars(),
                      Collectors.collectingAndThen(
                              Collectors.summingDouble(
                                      product -> product.getDiscount().doubleValue()),
                              discount -> formatter.moneyFormat.format(discount))));

    } finally{
      readLock.unlock();
    }
  }

  private static class ResourceFormatter {

    private Locale locale;
    private ResourceBundle resourceBundle;
    private DateTimeFormatter dateFormater;
    private NumberFormat moneyFormat;

    private ResourceFormatter(Locale locale) {
      this.locale = locale;
      resourceBundle = ResourceBundle.getBundle("resources", locale);
      dateFormater = DateTimeFormatter.ofLocalizedDate(FormatStyle.SHORT)
              .localizedBy(locale);
      moneyFormat = NumberFormat.getCurrencyInstance(locale);
    }

    private String formatProduct(Product product) {
      String type = switch (product) {
        case Food _ -> resourceBundle.getString("food");
        case Drink _ -> resourceBundle.getString("drink");
      };
      return MessageFormat.format(getText("product"),
              product.getName(),
              moneyFormat.format(product.getPrice()),
              product.getRating().getStars(),
              dateFormater.format(product.getBestBefore()),
              type);
    }

    private String formatReview(Review review) {
      return MessageFormat.format(getText("review"),
              review.rating().getStars(),
              review.comments());
    }

    private String getText(String key) {
      return resourceBundle.getString(key);
    }

  }
}
