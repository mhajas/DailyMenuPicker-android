package pv239.fi.muni.cz.dailymenupicker.parser

import org.jsoup.nodes.Element
import pv239.fi.muni.cz.dailymenupicker.parser.config.Day
import pv239.fi.muni.cz.dailymenupicker.parser.config.Source
import pv239.fi.muni.cz.dailymenupicker.parser.day.DateDayParser
import pv239.fi.muni.cz.dailymenupicker.parser.day.DayNameDayParser
import pv239.fi.muni.cz.dailymenupicker.parser.exception.UnknownDayParser

/**
 * @author mhajas
 */

fun getSourceParser(config: Source): TodayMenuParser<Any?> {
    return when (config.sourceParserType) {
        "WebPageHtmlParser" -> WebPageHtmlParser(config)
        else -> throw UnknownDayParser("Day parser doesn't exists with config: " + config.toString())
    }
}