package pv239.fi.muni.cz.dailymenupicker.parser

import pv239.fi.muni.cz.dailymenupicker.parser.entity.TodayMenuEntity

/**
 * @author mhajas
 */
abstract class TodayMenuParser<in INPUT_TYPE> : Parser<INPUT_TYPE, TodayMenuEntity>() {
}