package fr.outadoc.pavikt.api.server

import kotlinx.io.asSource
import kotlinx.io.buffered
import kotlinx.io.bytestring.ByteString
import kotlinx.io.readByteString

private object Resources

fun readResource(path: String): ByteString {
    return Resources::class.java.getResourceAsStream(path)!!
        .asSource()
        .buffered()
        .readByteString()
}
