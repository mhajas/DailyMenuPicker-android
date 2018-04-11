package pv239.fi.muni.cz.dailymenupicker.parser

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import pv239.fi.muni.cz.dailymenupicker.parser.entity.TodayMenuEntity
import pv239.fi.muni.cz.dailymenupicker.parser.config.Source
import pv239.fi.muni.cz.dailymenupicker.parser.menu.getMenuParser

/**
 * @author mhajas
 */
class WebPageHtmlParser(val config: Source, private val nextParser: TodayMenuParser<Document> = getMenuParser(config.menuParser)): TodayMenuParser<Any?>() {
    override fun parse(t: Any?): TodayMenuEntity {
        return nextParser.parse(Jsoup.connect(config.value).get())
    }
}