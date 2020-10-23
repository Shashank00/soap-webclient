package com.axis.webstarter.webclientsoap.encoders

import org.springframework.util.Assert
import java.util.concurrent.ConcurrentHashMap
import java.util.concurrent.ConcurrentMap
import javax.xml.bind.JAXBContext
import javax.xml.bind.JAXBException
import javax.xml.bind.Marshaller
import javax.xml.bind.Unmarshaller

class JaxbContextContainer {

    private val jaxbContexts: ConcurrentMap<Class<*>, JAXBContext> = ConcurrentHashMap(64)

    @Throws(JAXBException::class)
    fun createMarshaller(classType: Class<*>): Marshaller? {
        val jaxbContext = getJaxbContext(classType)
        return jaxbContext!!.createMarshaller()
    }

    @Throws(JAXBException::class)
    fun createUnmarshaller(classType: Class<*>): Unmarshaller? {
        val jaxbContext = getJaxbContext(classType)
        return jaxbContext!!.createUnmarshaller()
    }

    @Throws(JAXBException::class)
    private fun getJaxbContext(classType: Class<*>): JAXBContext? {
        Assert.notNull(classType, "Class must not be null")
        var jaxbContext: JAXBContext? = this.jaxbContexts.get(classType)
        if (jaxbContext == null) {
            jaxbContext = JAXBContext.newInstance(classType)
            this.jaxbContexts.putIfAbsent(classType, jaxbContext)
        }
        return jaxbContext;
    }
}