package org.joescaos.data;

@FunctionalInterface
public interface Rateable<T> {
     static final Rating DEFAULT_RATING = Rating.NOT_RATED;

    T applyRating(Rating rating);

     default T applyRating(int stars) {
        return applyRating(convert(stars));
    }

     default Rating getRating() {
        return DEFAULT_RATING;
    }

     static Rating convert(int stars) {
        return (stars>= 0 && stars <= 5) ? Rating.values() [stars]: DEFAULT_RATING;
    }
}
