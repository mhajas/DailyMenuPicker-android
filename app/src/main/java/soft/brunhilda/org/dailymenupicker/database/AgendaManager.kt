package soft.brunhilda.org.dailymenupicker.database

import com.orhanobut.hawk.Hawk
import soft.brunhilda.org.dailymenupicker.entity.DayOfWeek
import soft.brunhilda.org.dailymenupicker.entity.FoodEntityAdapterItem

class AgendaManager {

    fun addToAgenda(food: FoodEntityAdapterItem, dayOfWeek: DayOfWeek){
        if(isInAgenda(food, dayOfWeek)){
            return
        }
        val dayAgenda = getAgendaForDay(dayOfWeek)
        dayAgenda.add(food)
        Hawk.put(dayOfWeek.name, dayAgenda)
    }

    fun getAgendaForDay(dayOfWeek: DayOfWeek): MutableList<FoodEntityAdapterItem>{
        return Hawk.get<MutableList<FoodEntityAdapterItem>>(dayOfWeek.name, mutableListOf())
    }

    fun isInAgenda(food: FoodEntityAdapterItem, dayOfWeek: DayOfWeek): Boolean{
        val dayAgenda = getAgendaForDay(dayOfWeek)
        if(dayAgenda.isEmpty()){
            return false
        }
        return dayAgenda.filter { it.equals(food) }.isNotEmpty()
    }

    fun deleteFromAgenda(food: FoodEntityAdapterItem, dayOfWeek: DayOfWeek){
        if(!isInAgenda(food, dayOfWeek)){
            return
        }
        val dayAgenda = getAgendaForDay(dayOfWeek)
        if(dayAgenda.isEmpty()){
            return
        }
        dayAgenda.remove(food)
        Hawk.put(dayOfWeek.name, dayAgenda)
    }
}