package fr.outadoc.pavikt.api.data.serializer

import fr.outadoc.pavikt.api.data.model.CommandDTO
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object FunctionKeySetSerializer : KSerializer<Set<CommandDTO.FunctionKey>> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("FunctionKeySetSerializer", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Set<CommandDTO.FunctionKey> {
        val input = decoder.decodeInt()
        return CommandDTO.FunctionKey.entries
            .filter { functionKey ->
                input and functionKey.value != 0
            }
            .toSet()
    }

    override fun serialize(encoder: Encoder, value: Set<CommandDTO.FunctionKey>) {
        encoder.encodeInt(
            value.fold(0) { acc, functionKey ->
                acc or functionKey.value
            }
        )
    }
}
