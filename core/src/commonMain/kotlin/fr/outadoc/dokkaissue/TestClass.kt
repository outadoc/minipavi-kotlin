package fr.outadoc.dokkaissue

import kotlinx.io.bytestring.ByteString
import kotlinx.serialization.KSerializer

/**
 * This is a test class to demonstrate a Dokka issue with references to third-party libraries.
 *
 * @param url A URL.
 * @param number A number.
 * @param anotherRef A reference to another internal class.
 * @param bytes A byte string.
 * @param serializer A serializer.
 */
public data class TestClass(
    val url: String,
    val number: Int,
    val anotherRef: AnotherClass,
    val bytes: ByteString,
    val serializer: KSerializer<String>,
)

/**
 * This is another class.
 *
 * @param property A property.
 */
public data class AnotherClass(
    val property: String,
)

/***
 * This is a test function to demonstrate a Dokka issue with references to third-party libraries.
 *
 * @param url A URL.
 * @param number A number.
 * @param anotherRef A reference to another internal class.
 * @param bytes A byte string.
 * @param serializer A serializer.
 */
public fun testFunction(
    url: String,
    number: Int,
    anotherRef: AnotherClass,
    bytes: ByteString,
    serializer: KSerializer<String>,
) {
    println("Hello, world!")
}
