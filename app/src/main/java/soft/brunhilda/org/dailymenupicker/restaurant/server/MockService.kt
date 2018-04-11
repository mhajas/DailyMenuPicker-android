package soft.brunhilda.org.dailymenupicker.restaurant.server

import pv239.fi.muni.cz.dailymenupicker.parser.config.AttributeType
import pv239.fi.muni.cz.dailymenupicker.parser.config.Day
import pv239.fi.muni.cz.dailymenupicker.parser.config.Menu
import pv239.fi.muni.cz.dailymenupicker.parser.config.Source
import retrofit2.Call
import retrofit2.http.Path

/**
 * Created by mhajas on 4/10/18.
 */
class MockService {
    fun getParserConfig(placeId: String): Source {
        return Source("http://www.napurkynce.cz/denni-menu/","WebPageHtmlParser",
                Menu("ElementMenuParser",
                        Day("DayNameDayParser", ""), "pre", null, 1))
    }
}