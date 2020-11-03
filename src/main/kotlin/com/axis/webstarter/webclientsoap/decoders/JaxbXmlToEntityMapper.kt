package com.axis.webstarter.webclientsoap.decoders

import java.io.StringReader
import javax.xml.bind.JAXBContext

class JaxbXmlToEntityMapper {

    companion object {
        fun <T> unmarshall(xml: String, classType: Class<T>): T {
            val sr = StringReader(xml)
            val jaxbContext = JAXBContext.newInstance(classType)
            val jaxbUnmarshall = jaxbContext.createUnmarshaller()
            val obj = jaxbUnmarshall.unmarshal(sr)
            return classType.cast(obj)
        }
    }
}