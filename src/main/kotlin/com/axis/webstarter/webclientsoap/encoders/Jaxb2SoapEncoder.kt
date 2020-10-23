package com.axis.webstarter.webclientsoap.encoders

import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import org.reactivestreams.Publisher
import org.springframework.core.ResolvableType
import org.springframework.core.codec.CodecException
import org.springframework.core.codec.Encoder
import org.springframework.core.codec.EncodingException
import org.springframework.core.io.buffer.DataBuffer
import org.springframework.core.io.buffer.DataBufferFactory
import org.springframework.core.io.buffer.DataBufferUtils
import org.springframework.core.io.buffer.PooledDataBuffer
import org.springframework.util.ClassUtils
import org.springframework.util.MimeType
import org.springframework.util.MimeTypeUtils
import java.nio.charset.StandardCharsets
import java.util.function.Consumer
import javax.xml.bind.JAXBException
import javax.xml.bind.MarshalException
import javax.xml.bind.Marshaller
import javax.xml.bind.annotation.XmlRootElement
import javax.xml.bind.annotation.XmlType

class Jaxb2SoapEncoder: Encoder<Any> {

    private val jaxbContexts: JaxbContextContainer = JaxbContextContainer();

    override fun canEncode(elementType: ResolvableType, mimeType: MimeType?): Boolean {
        val outputClass = elementType.toClass()
        return outputClass.isAnnotationPresent(XmlRootElement::class.java) ||
                outputClass.isAnnotationPresent(XmlType::class.java)
    }

    override fun getEncodableMimeTypes(): MutableList<MimeType> = mutableListOf(MimeTypeUtils.TEXT_XML);

    override fun encode(publisher: Publisher<out Any>, dataBufferFactory : DataBufferFactory, resolvableType: ResolvableType, mimeType: MimeType?, hints: MutableMap<String, Any>?): Flux<DataBuffer> {
        return Flux.from(publisher)
                .take(1)
                .map{input: Any -> encodeValue(input , dataBufferFactory, resolvableType, mimeType, hints) }
                .doOnDiscard(PooledDataBuffer::class.java, Consumer { obj: PooledDataBuffer -> obj.release() })
    }

    private fun encodeValue(value: Any,
                       bufferFactory: DataBufferFactory,
                       type: ResolvableType,
                       mimeType: MimeType,
                       hints: MutableMap<String, Any>): Flux<DataBuffer>? {
        return Mono.fromCallable {
            var release = true
            val buffer = bufferFactory.allocateBuffer(1024)
            try {
                val outputStream = buffer.asOutputStream()
                val clazz = ClassUtils.getUserClass(value)
                val marshaller = initMarshaller(clazz)

                TODO("Enclose SOAP Part to request XML generated")
                /*val helper = DefaultStrategiesHelper(WebServiceTemplate::class.java)
                val messageFactory: WebServiceMessageFactory = helper.getDefaultStrategy(WebServiceMessageFactory::class.java)
                val message: WebServiceMessage = messageFactory.createWebServiceMessage()
                marshaller.marshal(value, message.getPayloadResult())
                message.writeTo(outputStream)*/
                release = false
                return@fromCallable buffer
            } catch (ex: MarshalException) {
                throw EncodingException(
                        "Could not marshal " + value.javaClass + " to XML", ex)
            } catch (ex: JAXBException) {
                throw CodecException("Invalid JAXB configuration", ex)
            } finally {
                if (release) {
                    DataBufferUtils.release(buffer)
                }
            }
        }.flux()
    }

    @Throws(JAXBException::class)
    private fun initMarshaller(clazz: Class<*>): Marshaller? {
        val marshaller: Marshaller? = this.jaxbContexts.createMarshaller(clazz)
        marshaller?.setProperty(Marshaller.JAXB_ENCODING, StandardCharsets.UTF_8.name())
        return marshaller;
    }
}