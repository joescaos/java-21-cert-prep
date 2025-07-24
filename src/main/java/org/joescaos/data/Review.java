package org.joescaos.data;

public record Review(Rating rating, String comments) implements Comparable<Review>{
    @Override
    public int compareTo(Review o) {
        return o.rating.ordinal() - this.rating.ordinal();
    }
}
