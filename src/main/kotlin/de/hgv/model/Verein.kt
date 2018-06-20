package de.hgv.model

import org.jsoup.Jsoup
import java.net.URL

/**
 * @author Florian Gutekunst
 */
open class Verein(val name: String, val id: String) {

    /**
     * Die wappenUrl wird beim ersten Zugriff heruntergeladen und gespeichert. Dieser dauert also potenziell etwas länger.
     */
    open val wappenURL: String? by lazy {
        val doc = Jsoup.parse(URL("http://weltfussball.de/teams/$id"), 5000)
        doc.selectFirst("div.emblem > a > img")?.attr("src")
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Verein

        if (name != other.name) return false
        if (id != other.id) return false

        return true
    }
}

object Unbekannt: Verein("unbekannt", "Unbekannt") {
    override val wappenURL = ""
}