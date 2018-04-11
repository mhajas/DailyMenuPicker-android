package pv239.fi.muni.cz.dailymenupicker.parser.config

/**
 * Created by mhajas on 3/17/18.
 */
data class Menu(
        val menuParserType: String,
        val dayParser: Day,

        val elementName: String,
        val elementAttribute: AttributeType?,
        val elementOrder: Int?)