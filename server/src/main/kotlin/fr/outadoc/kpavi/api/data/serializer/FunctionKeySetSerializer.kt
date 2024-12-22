package fr.outadoc.kpavi.api.data.serializer

import fr.outadoc.kpavi.api.data.model.Command
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object FunctionKeySetSerializer : KSerializer<Set<Command.FunctionKey>> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("FunctionKeySetSerializer", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Set<Command.FunctionKey> {
        val input = decoder.decodeInt()
        return Command.FunctionKey.entries
            .filter { functionKey ->
                input and functionKey.value != 0
            }
            .toSet()
    }

    override fun serialize(encoder: Encoder, value: Set<Command.FunctionKey>) {
        encoder.encodeInt(
            value.fold(0) { acc, functionKey ->
                acc or functionKey.value
            }
        )
    }
}
