package com.axis.webstarter.webclientsoap.utilities

import org.jsoup.Jsoup
import org.jsoup.nodes.Document
import org.jsoup.parser.Parser
import org.jsoup.select.Elements

class XmlProcessor {

    companion object {

        fun extractXmlElement(xmlString: String, xmlElement: String): String {
            val document: Document = Jsoup.parse(xmlString, "", Parser.xmlParser())
            document.outputSettings().prettyPrint(false)
            val targetElement: Elements = document.getElementsByTag(xmlElement)
            return targetElement.toString()
        }
    }
}