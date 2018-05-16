package soft.brunhilda.org.dailymenupicker.adapters

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.LinearLayout
import android.widget.TextView
import soft.brunhilda.org.dailymenupicker.ComparablePlace
import soft.brunhilda.org.dailymenupicker.R
import soft.brunhilda.org.dailymenupicker.database.AgendaManager
import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem
import java.text.NumberFormat
import java.util.*

class FoodEntityAdapter(
        val foodEntities: List<FoodEntityAdapterItem>,
        val callbackButton: (View, FoodEntityAdapterItem) -> Unit,
        val callbackView: (View, FoodEntityAdapterItem) -> Unit
): RecyclerView.Adapter<FoodEntityAdapter.ViewHolder>() {

    override fun onBindViewHolder(holder: ViewHolder?, position: Int) {
        val food = foodEntities[position]

        holder?.food_name?.text = food.foodEntity.name.toLowerCase().capitalize()

        val currencyFormater = NumberFormat.getCurrencyInstance()
        currencyFormater.currency = Currency.getInstance("CZK")
        holder?.food_price?.text = currencyFormater.format(food.foodEntity.price)

        holder?.soup?.text = food.restaurantDailyData.soup[0].name.toLowerCase().capitalize() // TODO: add support for more soups from one restaurant

        if (food.restaurantDailyData.soup[0].price == null) {
            holder?.soup_price?.text = "v cene"
        }else{
            holder?.soup_price?.text = currencyFormater.format(food.restaurantDailyData.soup[0].price)
        }

        holder?.restaurant_name?.text =  food.googlePlace.name

        holder?.evaluation?.text = String.format("%.2f", food.preferenceEvaluation)

        holder?.button?.isChecked = AgendaManager().isInAgenda(food, food.dayOfWeek)

        holder?.button?.setOnClickListener {
            callbackButton(it, food)
        }

        holder?.itemView?.setOnClickListener {
            callbackView(it, food)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup?, viewType: Int): ViewHolder {
        val v = LayoutInflater.from(parent?.context).inflate(R.layout.list_item_dayfood, parent, false)
        return ViewHolder(v)
    }

    override fun getItemCount(): Int {
        return foodEntities.size
    }

    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val food_name = itemView.findViewById<TextView>(R.id.dayfood_item_name)
        val food_price = itemView.findViewById<TextView>(R.id.dayfood_item_price)
        val soup = itemView.findViewById<TextView>(R.id.dayfood_soup)
        val soup_price = itemView.findViewById<TextView>(R.id.dayfood_soup_price)
        val restaurant_name = itemView.findViewById<TextView>(R.id.restaurant_name)
        val evaluation = itemView.findViewById<TextView>(R.id.dayfood_evaluation)
        val button = itemView.findViewById<CheckBox>(R.id.agenda_button)
    }
}