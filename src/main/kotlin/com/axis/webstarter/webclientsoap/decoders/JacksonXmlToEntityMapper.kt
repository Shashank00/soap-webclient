package com.axis.webstarter.webclientsoap.decoders

import com.fasterxml.jackson.dataformat.xml.XmlMapper

final class JacksonXmlToEntityMapper {

    fun <T> unmarshall(xml: String, classType: Class<T>): T {
        val xmlMapper: XmlMapper = XmlMapper();
        val entity = xmlMapper.readValue(xml, classType);
        return entity;
    }
}