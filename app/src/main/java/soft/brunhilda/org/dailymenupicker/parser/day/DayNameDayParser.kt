package pv239.fi.muni.cz.dailymenupicker.parser.day

import org.jsoup.nodes.Element
import pv239.fi.muni.cz.dailymenupicker.parser.TodayMenuParser
import pv239.fi.muni.cz.dailymenupicker.parser.config.Day
import pv239.fi.muni.cz.dailymenupicker.parser.entity.FoodEntity
import pv239.fi.muni.cz.dailymenupicker.parser.entity.TodayMenuEntity
import java.util.Calendar



/**
 * Created by mhajas on 3/17/18.
 */
class DayNameDayParser(val config: Day) : TodayMenuParser<Element>() {

    private val days: Array<String> = arrayOf("PONDĚLÍ", "ÚTERÝ", "STŘEDA", "ČTVRTEK", "PÁTEK") // TODO: Move to property file, possible future localization

    override fun parse(t: Element): TodayMenuEntity {
        var menu = t.textNodes().map { textNode -> textNode.text() }
        menu = menu.dropWhile{ s -> !containsTodayDayName(s)}.takeWhile { s -> !containsTomorrow(s) }

        return TodayMenuEntity(
                menu.first(),
                menu.drop(1).map { s -> FoodEntity(s) })
    }


    private fun containsTodayDayName(dayName: String): Boolean{
        val calendar = Calendar.getInstance()
        val todayNumber = calendar.get(Calendar.DAY_OF_WEEK)

        return dayName.contains(days[todayNumber - 2], true) // -1 indexing from 0 and -1 for starting with sunday in Calendar
    }

    private fun containsTomorrow(dayName: String): Boolean{
        val calendar = Calendar.getInstance()
        val tomorrowNumber = calendar.get(Calendar.DAY_OF_WEEK) + 1

        return dayName.contains(days[tomorrowNumber - 2], true) // -1 indexing from 0 and -1 for starting with sunday in Calendar
    }


}