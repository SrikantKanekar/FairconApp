package com.example.faircon.framework.datasource.network.mappers

import com.example.faircon.HomePreferences.Mode
import com.example.faircon.HomePreferences.Status
import com.example.faircon.business.domain.model.Parameter
import com.example.faircon.business.domain.util.DomainMapper
import com.example.faircon.framework.datasource.network.response.ParameterResponse

class ParameterMapper: DomainMapper<ParameterResponse, Parameter> {
    override fun mapToDomainModel(model: ParameterResponse): Parameter {
        return Parameter(
            fanSpeed = model.fanSpeed,
            roomTemperature = model.temperature,
            tecVoltage = model.tecVoltage,
            powerConsumption = model.powerConsumption,
            heatExpelling = model.heatExpelling,
            tecTemperature = model.tecTemperature,
            mode = Mode.forNumber(model.mode),
            status = Status.forNumber(model.status)
        )
    }

    override fun mapFromDomainModel(domainModel: Parameter): ParameterResponse {
        return ParameterResponse(
            fanSpeed = domainModel.fanSpeed,
            temperature = domainModel.roomTemperature,
            tecVoltage = domainModel.tecVoltage,
            powerConsumption = domainModel.powerConsumption,
            heatExpelling = domainModel.heatExpelling,
            tecTemperature = domainModel.tecTemperature,
            mode = domainModel.mode.number,
            status = domainModel.status.number
        )
    }
}