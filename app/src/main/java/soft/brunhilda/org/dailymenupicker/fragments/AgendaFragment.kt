package soft.brunhilda.org.dailymenupicker.fragments

import android.os.Bundle
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
        setDataForDate(day_view_monday, agendaManager.getAgendaForDay(DayOfWeek.MONDAY), DayOfWeek.MONDAY)
        setDataForDate(day_view_tuesday, agendaManager.getAgendaForDay(DayOfWeek.TUESDAY), DayOfWeek.TUESDAY)
        setDataForDate(day_view_wednesday, agendaManager.getAgendaForDay(DayOfWeek.WEDNESDAY), DayOfWeek.WEDNESDAY)
        setDataForDate(day_view_thursday, agendaManager.getAgendaForDay(DayOfWeek.THURSDAY), DayOfWeek.THURSDAY)
        setDataForDate(day_view_friday, agendaManager.getAgendaForDay(DayOfWeek.FRIDAY), DayOfWeek.FRIDAY)
    }

    private fun setDataForDate(view: RecyclerView, food: List<FoodEntityAdapterItem>, dayOfWeek: DayOfWeek){
        view.layoutManager = LinearLayoutManager(context, LinearLayout.VERTICAL, false)
        view.adapter = FoodEntityAdapter(
                food,
                ButtonManager().inAgendaButton(activity, context, dayOfWeek),
                ButtonManager().goToRestaurant(activity)
        )
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater?.inflate(R.layout.list_days, container, false)
    }
}