package com.b4t.app.service.mapper;


import com.b4t.app.domain.*;
import com.b4t.app.service.UserService;
import com.b4t.app.service.dto.ChartDescriptionDTO;

import com.b4t.app.service.dto.UserDTO;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Service
public class ChartDescriptionCustomMapper {
    @Autowired
    UserService userService;

    public List<ChartDescriptionDTO> chartDescriptionsToChartDescriptionDTOs(List<ChartDescription> chartDescriptions) {
        return chartDescriptions.stream()
            .filter(Objects::nonNull)
            .map(this::chartDescriptionToChartDescriptionDTO)
            .collect(Collectors.toList());
    }

    public ChartDescriptionDTO chartDescriptionToChartDescriptionDTO(ChartDescription chartDescription) {
        UserDTO userDTO1 = userService.getById(chartDescription.getModifiedBy());
        UserDTO userDTO2 = userService.getById(chartDescription.getCreatedBy());
        return new ChartDescriptionDTO(chartDescription, userDTO1.getLogin(), userDTO2.getLogin());
    }

    public List<ChartDescription> chartDescriptionDTOsToChartDescriptions(List<ChartDescriptionDTO> chartDescriptionDTOS) {
        return chartDescriptionDTOS.stream()
            .filter(Objects::nonNull)
            .map(this::chartDescriptionDTOToChartDescription)
            .collect(Collectors.toList());
    }

    public ChartDescription chartDescriptionDTOToChartDescription(ChartDescriptionDTO chartDescriptionDTO) {
        if (chartDescriptionDTO == null) {
            return null;
        } else {
            UserDTO userDTO1 = userService.getByLogin(chartDescriptionDTO.getModifiedBy());
            UserDTO userDTO2 = userService.getByLogin(chartDescriptionDTO.getCreatedBy());
            return new ChartDescription(chartDescriptionDTO, userDTO1.getId(), userDTO2.getId());
        }
    }
}
