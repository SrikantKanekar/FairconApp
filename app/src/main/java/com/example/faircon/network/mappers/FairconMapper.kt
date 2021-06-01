package com.example.faircon.network.mappers

import com.example.faircon.model.*
import com.example.faircon.util.DomainMapper
import com.example.faircon.network.response.FairconResponse

class FairconMapper: DomainMapper<FairconResponse, Faircon> {
    override fun mapToDomainModel(model: FairconResponse): Faircon {
        return Faircon(
            parameter = Parameter(
                fanSpeed = model.fanSpeed,
                roomTemperature = model.roomTemperature,
                tecVoltage = model.tecVoltage,
                powerConsumption = model.powerConsumption,
                heatExpelling = model.heatExpelling,
                tecTemperature = model.tecTemperature,
            ),
            controller = Controller(
                fanSpeed = model.requiredFanSpeed,
                temperature = model.requiredTemperature,
                tecVoltage = model.requitedTecVoltage,
            ),
            mode = Mode.values()[model.mode] ,
            status = Status.values()[model.status]
        )
    }

    override fun mapFromDomainModel(domainModel: Faircon): FairconResponse {
        return FairconResponse(
            fanSpeed = domainModel.parameter.fanSpeed,
            roomTemperature = domainModel.parameter.roomTemperature,
            tecVoltage = domainModel.parameter.tecVoltage,
            powerConsumption = domainModel.parameter.powerConsumption,
            heatExpelling = domainModel.parameter.heatExpelling,
            tecTemperature = domainModel.parameter.tecTemperature,
            requiredFanSpeed = domainModel.controller.fanSpeed,
            requiredTemperature = domainModel.controller.temperature,
            requitedTecVoltage = domainModel.controller.tecVoltage,
            mode = domainModel.mode.ordinal,
            status = domainModel.status.ordinal
        )
    }
}