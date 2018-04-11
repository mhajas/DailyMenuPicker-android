package pv239.fi.muni.cz.dailymenupicker.parser.config

/**
 * @author mhajas
 */
data class Source (
        val value: String,
        val sourceParserType: String,
        val menuParser: Menu)