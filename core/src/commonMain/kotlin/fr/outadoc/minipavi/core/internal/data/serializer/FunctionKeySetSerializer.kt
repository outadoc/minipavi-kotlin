package fr.outadoc.minipavi.core.internal.data.serializer

import fr.outadoc.minipavi.core.internal.data.model.ServiceResponseDTO
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder

internal object FunctionKeySetSerializer : KSerializer<Set<ServiceResponseDTO.Command.FunctionKey>> {

    override val descriptor: SerialDescriptor =
        PrimitiveSerialDescriptor("FunctionKeySetSerializer", PrimitiveKind.INT)

    override fun deserialize(decoder: Decoder): Set<ServiceResponseDTO.Command.FunctionKey> {
        val input = decoder.decodeInt()
        return ServiceResponseDTO.Command.FunctionKey.entries
            .filter { functionKey ->
                input and functionKey.value != 0
            }
            .toSet()
    }

    override fun serialize(encoder: Encoder, value: Set<ServiceResponseDTO.Command.FunctionKey>) {
        encoder.encodeInt(
            value.fold(0) { acc, functionKey ->
                acc or functionKey.value
            }
        )
    }
}
