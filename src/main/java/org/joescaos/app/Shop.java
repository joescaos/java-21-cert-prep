package org.joescaos.app;

import java.math.BigDecimal;
import org.joescaos.data.Product;
import org.joescaos.data.Rating;

public class Shop {

  public static void main(String[] args) {
    Product p1 = new Product();

    System.out.printf(
        "%d %s %s %s %s%n",
        p1.getId(), p1.getName(), p1.getPrice(), p1.getDiscount(), p1.getRating().getStars());

    Product p2 = new Product(102, "Cake", BigDecimal.valueOf(1.98));
    System.out.printf(
        "%d %s %s %s %s%n",
        p2.getId(), p2.getName(), p2.getPrice(), p2.getDiscount(), p2.getRating().getStars());

    Product p3 = new Product(103, "Coffee", BigDecimal.valueOf(1.98), Rating.FIVE_STAR);
    System.out.printf(
        "%d %s %s %s %s%n",
        p3.getId(), p3.getName(), p3.getPrice(), p3.getDiscount(), p3.getRating().getStars());

    Product p5 = p2.applyRating(Rating.THREE_STAR);
    System.out.printf(
            "%d %s %s %s %s%n",
            p5.getId(), p5.getName(), p5.getPrice(), p5.getDiscount(), p5.getRating().getStars());
  }
}
