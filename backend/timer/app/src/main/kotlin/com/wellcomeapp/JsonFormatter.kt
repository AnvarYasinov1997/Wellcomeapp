package com.wellcomeapp

import java.io.PrintWriter
import java.io.StringWriter
import java.util.*
import java.util.logging.Formatter
import java.util.logging.LogRecord


class JsonFormatter : Formatter() {
    private val dat by lazy { Date() }

    override fun format(record: LogRecord): String {
        dat.time = record.millis
        var source: String
        if (record.sourceClassName != null) {
            source = record.sourceClassName
            if (record.sourceMethodName != null) {
                source += " " + record.sourceMethodName
            }
        } else {
            source = record.loggerName
        }
        val message = formatMessage(record)
        var throwable = ""
        if (record.thrown != null) {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            pw.println()
            record.thrown.printStackTrace(pw)
            pw.close()
            throwable = sw.toString()
        }
        return Log(dat, source, record.loggerName, record.level.localizedName, message, throwable).toString() +'\n'
    }

    data class Log(val date: Date,
                   val source: String,
                   val name: String,
                   val levelName: String,
                   val message: String,
                   val throwable: String){
        override fun toString(): String {
            return "{" +
                    "date: \"$date\", " +
                    "source: \"$source\", " +
                    "name: \"$name\", " +
                    "levelName: \"$levelName\", " +
                    "message: \"$message\", " +
                    "throwable: \"$throwable\"" +
                    "}"
        }
    }
}