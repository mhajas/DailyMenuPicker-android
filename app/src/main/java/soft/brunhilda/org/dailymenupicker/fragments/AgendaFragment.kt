package soft.brunhilda.org.dailymenupicker.fragments

import android.os.Bundle
import android.support.constraint.ConstraintLayout
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import kotlinx.android.synthetic.main.list_days.*
import soft.brunhilda.org.dailymenupicker.R
import soft.brunhilda.org.dailymenupicker.adapters.FoodEntityAdapter
import soft.brunhilda.org.dailymenupicker.entity.DayOfWeek
import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem

class AgendaFragment : ParentFragment(){

    companion object {
        private var mInstance: AgendaFragment = AgendaFragment()

        @Synchronized
        fun getInstance(): AgendaFragment {
            return mInstance
        }
    }

    private val agendaManager = soft.brunhilda.org.dailymenupicker.database.AgendaManager()

    override fun onViewCreated(view: View?, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setDataForDate(day_view_monday, monday_no_food_message, agendaManager.getAgendaForDay(DayOfWeek.MONDAY), DayOfWeek.MONDAY)
        setDataForDate(day_view_tuesday, tuesday_no_food_message, agendaManager.getAgendaForDay(DayOfWeek.TUESDAY), DayOfWeek.TUESDAY)
        setDataForDate(day_view_wednesday, wednesday_no_food_message, agendaManager.getAgendaForDay(DayOfWeek.WEDNESDAY), DayOfWeek.WEDNESDAY)
        setDataForDate(day_view_thursday, thursday_no_food_message, agendaManager.getAgendaForDay(DayOfWeek.THURSDAY), DayOfWeek.THURSDAY)
        setDataForDate(day_view_friday, friday_no_food_message, agendaManager.getAgendaForDay(DayOfWeek.FRIDAY), DayOfWeek.FRIDAY)
    }

    private fun setDataForDate(view: RecyclerView, noFoodView: ConstraintLayout, food: List<FoodEntityAdapterItem>, dayOfWeek: DayOfWeek){
        if (food.isNotEmpty()) {
            view.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
            view.adapter = FoodEntityAdapter(
                    food,
                    ButtonManager().inAgendaButton(activity, context, dayOfWeek),
                    ButtonManager().goToRestaurant(activity)
            )

            view.visibility = View.VISIBLE
            noFoodView.visibility = View.GONE
        } else {
            noFoodView.visibility = View.VISIBLE
            view.visibility = View.GONE
        }
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.content_scrolling, container, false)
    }
}