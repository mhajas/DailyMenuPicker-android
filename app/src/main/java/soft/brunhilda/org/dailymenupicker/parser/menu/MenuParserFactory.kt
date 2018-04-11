package pv239.fi.muni.cz.dailymenupicker.parser.menu

import org.jsoup.nodes.Document
import pv239.fi.muni.cz.dailymenupicker.parser.Parser
import pv239.fi.muni.cz.dailymenupicker.parser.TodayMenuParser
import pv239.fi.muni.cz.dailymenupicker.parser.config.Menu
import pv239.fi.muni.cz.dailymenupicker.parser.entity.TodayMenuEntity
import pv239.fi.muni.cz.dailymenupicker.parser.exception.UnknownMenuParser

/**
 * Created by mhajas on 3/18/18.
 */
fun getMenuParser(config: Menu): TodayMenuParser<Document> {
    return when (config.menuParserType) {
        "ElementMenuParser" -> ElementMenuParser(config)
        else -> throw UnknownMenuParser("Menu config doesn't exists with config: " + config.toString())
    }
}