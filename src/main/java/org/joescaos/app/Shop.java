package org.joescaos.app;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Locale;

import org.joescaos.data.Drink;
import org.joescaos.data.Food;
import org.joescaos.data.Product;
import org.joescaos.data.ProductManager;
import org.joescaos.data.Rating;

public class Shop {

  public static void main(String[] args) {
    ProductManager productManager = new ProductManager(Locale.US);
    Product p1 = productManager.createProduct(101, "Tea", BigDecimal.valueOf(1.99), Rating.NOT_RATED);
    productManager.printProductReport(101);
    productManager.reviewProduct(101, Rating.FIVE_STAR, "Nice coup of tea");
    productManager.reviewProduct(101, Rating.TWO_STAR, "Rather weak tea");
    productManager.reviewProduct(101, Rating.FOUR_STAR, "Fine tea");
    productManager.reviewProduct(101, Rating.FOUR_STAR, "Good tea");
    productManager.reviewProduct(101, Rating.THREE_STAR, "Just add some lemon");
    productManager.printProductReport(101);
    Product p2 = productManager.createProduct(102, "Cake", BigDecimal.valueOf(1.98), Rating.NOT_RATED,
            LocalDate.now().plusDays(2));
    productManager.printProductReport(102);
    productManager.reviewProduct(102, Rating.FIVE_STAR, "Nice piece of cake");
    productManager.reviewProduct(102, Rating.TWO_STAR, "too expensive");
    productManager.printProductReport(102);
//
//    Product p3 = productManager.createProduct(103, "Coffee", BigDecimal.valueOf(1.98), Rating.FIVE_STAR);
//
//    Product p5 = p2.applyRating(Rating.THREE_STAR);
//
//    System.out.println(p1);
//    System.out.println(p2);
//    System.out.println(p3);
//    System.out.println(p5);
//    System.out.println(p2.equals(p5));
//    System.out.println(p2.getBestBefore());
//    System.out.println(p3.getBestBefore());
  }
}
