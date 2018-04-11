package pv239.fi.muni.cz.dailymenupicker.parser

/**
 * Created by mhajas on 3/17/18.
 */
abstract class Parser<in INPUT_TYPE, out RESULTING_TYPE> {
    abstract fun parse(t: INPUT_TYPE) : RESULTING_TYPE
}