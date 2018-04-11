package pv239.fi.muni.cz.dailymenupicker.parser.day

import org.jsoup.nodes.Element
import pv239.fi.muni.cz.dailymenupicker.parser.Parser
import pv239.fi.muni.cz.dailymenupicker.parser.TodayMenuParser
import pv239.fi.muni.cz.dailymenupicker.parser.config.Day
import pv239.fi.muni.cz.dailymenupicker.parser.entity.TodayMenuEntity
import pv239.fi.muni.cz.dailymenupicker.parser.exception.UnknownDayParser

/**
 * Created by mhajas on 3/17/18.
 */

fun getDayParser(config: Day): TodayMenuParser<Element> {
    return when (config.dayParserType) {
        "DayNameDayParser" -> DayNameDayParser(config)
        "DateDayParser" -> DateDayParser(config)
        else -> throw UnknownDayParser("Day parser doesn't exists with config: " + config.toString())
    }
}