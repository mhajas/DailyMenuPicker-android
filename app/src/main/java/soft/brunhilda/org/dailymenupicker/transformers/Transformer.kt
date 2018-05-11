package soft.brunhilda.org.dailymenupicker.transformers

import noman.googleplaces.Place
import soft.brunhilda.org.dailymenupicker.entity.RestaurantWeekData

interface Transformer<T> {
    fun transform(from: Map<Place, RestaurantWeekData?>): MutableList<T>
}