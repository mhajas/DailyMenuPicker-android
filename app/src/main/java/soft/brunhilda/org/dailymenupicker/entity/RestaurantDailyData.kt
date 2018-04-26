package soft.brunhilda.org.dailymenupicker.entity

import noman.googleplaces.Place

/**
 * Created by mhajas on 4/21/18.
 */
data class RestaurantDailyData (var googlePlaceData: Place, var restaurantName: String, var soup: List<FoodEntity>, var menu: List<FoodEntity>, var soupIncludedInPrice: Boolean) {

    constructor(googlePlaceData: Place, dataTransferObject: RestaurantDailyDataTransferObject)
            : this(googlePlaceData, dataTransferObject.restaurantName, dataTransferObject.soup, dataTransferObject.menu, dataTransferObject.soupIncludedInPrice)

}