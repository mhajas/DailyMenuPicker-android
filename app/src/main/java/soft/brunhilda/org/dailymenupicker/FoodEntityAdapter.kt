package soft.brunhilda.org.dailymenupicker

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import pv239.fi.muni.cz.dailymenupicker.parser.entity.FoodEntity

class FoodEntityAdapter(
		private val context: Context,
		private val foodEntities: List<FoodEntity>
) : BaseAdapter() {
	override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View? {
		val food = getItem(position)

		var view = convertView

		if (view == null) {
			view = LayoutInflater.from(context).inflate(R.layout.list_item_dayfood, parent, false)
		}

		view?.findViewById<TextView>(R.id.dayfood_item_name)?.text = food.name
		//view?.findViewById<TextView>(R.id.dayfood_item_price)?.text = food.

		return view
	}

	override fun getItem(position: Int): FoodEntity = foodEntities[position]

	override fun getItemId(position: Int): Long = position.toLong()

	override fun getCount(): Int = foodEntities.size

}
