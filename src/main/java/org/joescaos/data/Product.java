package org.joescaos.data;

import static java.math.RoundingMode.HALF_UP;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.Objects;

public abstract sealed class Product implements Rateable<Product>
        permits Food, Drink {

  private final int id;
  private final String name;
  private final BigDecimal price;
  public static final BigDecimal DISCOUNT_RATE = BigDecimal.valueOf(0.1);
  private final Rating rating;

  protected Product(int id, String name, BigDecimal price, Rating rating) {
    this.id = id;
    this.name = name;
    this.price = price;
    this.rating = rating;
  }

  public int getId() {
    return id;
  }

  public String getName() {
    return name;
  }

  public BigDecimal getPrice() {
    return price;
  }

  public BigDecimal getDiscount() {
    return price.multiply(DISCOUNT_RATE).setScale(2, HALF_UP);
  }

  public Rating getRating() {
    return this.rating;
  }

  public LocalDate getBestBefore() {
    return LocalDate.now();
  }

  public abstract Product applyRating(Rating newRating);

  @Override
  public String toString() {
    return id
        + ", "
        + name
        + ", "
        + price
        + ", "
        + getDiscount()
        + ", "
        + rating.getStars()
        + ", "
        + getBestBefore();
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof Product product)) return false;
    return id == product.id && Objects.equals(name, product.name);
  }

  @Override
  public int hashCode() {
    return Objects.hashCode(id);
  }
}
