package pv239.fi.muni.cz.dailymenupicker.parser.menu

import org.jsoup.nodes.Document
import org.jsoup.nodes.Element
import pv239.fi.muni.cz.dailymenupicker.parser.Parser
import pv239.fi.muni.cz.dailymenupicker.parser.TodayMenuParser
import pv239.fi.muni.cz.dailymenupicker.parser.config.Menu
import pv239.fi.muni.cz.dailymenupicker.parser.day.getDayParser
import pv239.fi.muni.cz.dailymenupicker.parser.entity.TodayMenuEntity
import pv239.fi.muni.cz.dailymenupicker.parser.exception.AmbiguosMenuElement

/**
 * Created by mhajas on 3/17/18.
 */

class ElementMenuParser(val config: Menu, val nextParser: TodayMenuParser<Element> = getDayParser(config.dayParser)) : TodayMenuParser<Document>() {
    override fun parse(t: Document): TodayMenuEntity {
        t.run {

            var elements = getElementsByTag(config.elementName).toList()
            if(elements.size == 1) {
                return nextParser.parse(elements.first())
            }

            if (config.elementAttribute != null) {
                elements = elements.filter { element ->
                    element.attributes().any { it.key == config.elementAttribute.attributeName && it.value.contains(config.elementAttribute.attributeValue, true) }
                }
            }

            if(elements.size == 1) {
                return nextParser.parse(elements.first())
            }

            if (config.elementOrder != null) {
                return nextParser.parse(elements.get(config.elementOrder))
            }

            return nextParser.parse(elements.first())
        }
    }
}