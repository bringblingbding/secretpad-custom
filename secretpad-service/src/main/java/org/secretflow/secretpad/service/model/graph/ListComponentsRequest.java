package org.secretflow.secretpad.service.model.graph;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

import java.util.List;

@Data
public class ListComponentsRequest {
    public String projectId;
}