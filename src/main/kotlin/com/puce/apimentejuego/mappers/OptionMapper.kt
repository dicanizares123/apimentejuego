package com.puce.apimentejuego.mappers

import com.puce.apimentejuego.models.entities.Option
import com.puce.apimentejuego.models.requests.OptionRequest
import com.puce.apimentejuego.models.responses.OptionResponse
import org.springframework.stereotype.Component


@Component
class OptionMapper {

    fun toEntity(request: OptionRequest): Option {
        return Option(
            possibleAnswer = request.possibleAnswer
        )
    }

    fun toResponse(option: Option): OptionResponse{
        return OptionResponse(
            id = option.id,
            possibleAnswer = option.possibleAnswer
        )
    }

}